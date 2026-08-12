package com.example.pingstack.email.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.dto.UpdateDomainRequest;
import com.example.pingstack.email.dto.brevo.*;
import com.example.pingstack.email.mapper.BrevoDomainMapper;
import com.example.pingstack.email.model.EmailConfig;
import com.example.pingstack.email.model.domain.BrevoDomain;
import com.example.pingstack.email.model.domain.DnsRecord;
import com.example.pingstack.email.repository.BrevoDomainRepository;
import com.example.pingstack.email.repository.DnsProviderRepository;
import com.example.pingstack.email.repository.EmailConfigRepository;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import com.example.pingstack.exceptions.unauthorized.AccessDeniedException;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class BrevoDomainService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Autowired
    private EmailConfigRepository emailConfigRepository;
    @Autowired
    private BrevoDomainRepository brevoDomainRepository;
    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;
    @Autowired
    private DnsProviderRepository dnsProviderRepository;
    @Autowired
    private BrevoDomainMapper brevoDomainMapper;
    private static final String BREVO_DOMAINS_URL = "https://api.brevo.com/v3/senders/domains";


    public BrevoDomainDto registerDomain(String authHeader, UpdateDomainRequest updateDomainRequest){
        System.out.println(updateDomainRequest.getCustomDomain());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key", brevoApiKey);
        Map<String, Object> body = Map.of("name", updateDomainRequest.getCustomDomain());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("email config not found"));
        if( emailConfig.getCustomDomain()==null || emailConfig.getCustomDomain().isBlank() ){
            emailConfig.setCustomDomain(emailConfig.getCustomDomain());
            emailConfigRepository.save(emailConfig);
        }
        try {
            ResponseEntity<BrevoDomainDto> response = restTemplate.postForEntity(BREVO_DOMAINS_URL, request, BrevoDomainDto.class);
            BrevoDomain brevoDomain = brevoDomainMapper.toBrevo(Objects.requireNonNull(response.getBody()));
            brevoDomain.setEmailConfig(emailConfig);
            brevoDomainRepository.save(brevoDomain);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public DomainVerificationDto verifyDomain(String authHeader, VerifyDomainRequest verifyDomainRequest){
        if (verifyDomainRequest == null || verifyDomainRequest.getDomainName() == null || verifyDomainRequest.getDomainName().isBlank()) {
            throw new IllegalArgumentException("Domain name must not be empty or null");
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key", brevoApiKey);
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
        String url = BREVO_DOMAINS_URL + "/{domainName}";
        try {
            ResponseEntity<DomainVerificationDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    DomainVerificationDto.class,
                    verifyDomainRequest.getDomainName()
            );
            DomainVerificationDto verificationResult = response.getBody();
            if (verificationResult == null) {
                throw new RuntimeException("Received empty response from Brevo API");
            }
            User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
            EmailConfig emailConfig =  emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("email config not found"));
            BrevoDomain brevoDomain = brevoDomainRepository.findById(verifyDomainRequest.getDomainId()).orElseThrow(() -> new ResourceNotFoundException("Domain not found"));
            List<DnsRecord> dnsRecords = dnsProviderRepository.findByBrevoDomain(brevoDomain).orElseThrow(() -> new ResourceNotFoundException("Dns config is empty"));
            Map<String, DnsRecordDto> brevoRecords = verificationResult.getDns_records();
            for(DnsRecord dnsRecord : dnsRecords){
                DnsRecordDto dnsRecordDto = brevoRecords.get(dnsRecord.getRecordName());
                if(dnsRecordDto != null){
                    dnsRecord.setVerified(dnsRecordDto.isStatus());
                }
            }
            dnsProviderRepository.saveAll(dnsRecords);
            brevoDomain.setVerified(verificationResult.isVerified());
            brevoDomain.setAuthenticated(verificationResult.isAuthenticated());
            brevoDomainRepository.save(brevoDomain);
            return verificationResult;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to verify domain with Brevo API: " + e.getMessage(), e);
        }
    }

    public List<AllBrevoDomainDto> getAllDomainsWithDnsConfig (String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Email config not found"));
        List<BrevoDomain> domains = brevoDomainRepository.findByEmailConfig(emailConfig);
        return domains.stream().map(brevoDomainMapper::toResponse).toList();
    }

    public AllBrevoDomainDto getSingleDomainDetail(String domainId) {
        BrevoDomain brevoDomain = brevoDomainRepository.findById(domainId).orElseThrow(()-> new ResourceNotFoundException("domain not found"));
        return  brevoDomainMapper.toResponse(brevoDomain);
    }

    public AllBrevoDomainDto warningBeforeMakingPrimary (String authHeader , String domainId) {
        System.out.println("DOMAIN ID RECEIVED: " + domainId);

        BrevoDomain brevoDomain = brevoDomainRepository
                .findById(domainId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "SELECTED DOMAIN NOT FOUND: " + domainId
                        ));

        System.out.println(
                "FOUND DOMAIN: " + brevoDomain.getDomainName()
        );
        if(brevoDomain.isPrimary()){
            throw new IllegalArgumentException("Domain Already Primary");
        }
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Email config not found"));
        if(!brevoDomain.getEmailConfig().equals(emailConfig)){
            throw new AccessDeniedException("Domain does not belong to this user");
        }
        BrevoDomain primaryDomain = brevoDomainRepository.findByIsPrimaryTrueAndEmailConfig(emailConfig).orElseThrow(() -> new ResourceNotFoundException("Domain not found"));
        return brevoDomainMapper.toResponse(primaryDomain);
    }


    public AllBrevoDomainDto makeDomainPrimary (String authHeader , String domainId) {
        BrevoDomain brevoDomain = brevoDomainRepository.findById(domainId).orElseThrow(() -> new ResourceNotFoundException("domain not found "));
        if(brevoDomain.isPrimary()){
            throw new IllegalArgumentException("Domain Already Primary");
        }
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Email config not found"));
        if(!brevoDomain.getEmailConfig().equals(emailConfig)){
            throw new AccessDeniedException("Domain does not belong to this user");
        }
        Optional<BrevoDomain> primaryDomain = brevoDomainRepository.findByIsPrimaryTrueAndEmailConfig(emailConfig);
        primaryDomain.ifPresent(domain -> {
            domain.setPrimary(false);
            brevoDomainRepository.save(domain);
        });
        brevoDomain.setPrimary(true);
        brevoDomainRepository.save(brevoDomain);
        return brevoDomainMapper.toResponse(brevoDomain);
    }



    public String deleteDomain (String domainId, String authHeader) {
        BrevoDomain brevoDomain = brevoDomainRepository.findById(domainId).orElseThrow(() -> new ResourceNotFoundException("domain not found"));
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Email config not found for the user "));

        if(!brevoDomain.getEmailConfig().getUserId().equals(emailConfig.getUserId())){
            throw new AccessDeniedException("You can not delete this domain");
        }
         brevoDomainRepository.delete(brevoDomain);

        return brevoDomain.getDomainName() + "Deleted successfully";
    }





}
