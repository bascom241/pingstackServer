package com.example.pingstack.email.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.dto.UpdateDomainRequest;
import com.example.pingstack.email.dto.brevo.BrevoDomainDto;
import com.example.pingstack.email.dto.brevo.DnsRecordDto;
import com.example.pingstack.email.dto.brevo.DomainVerificationDto;
import com.example.pingstack.email.dto.brevo.VerifyDomainRequest;
import com.example.pingstack.email.mapper.BrevoDomainMapper;
import com.example.pingstack.email.model.EmailConfig;
import com.example.pingstack.email.model.domain.BrevoDomain;
import com.example.pingstack.email.model.domain.DnsRecord;
import com.example.pingstack.email.repository.BrevoDomainRepository;
import com.example.pingstack.email.repository.DnsProviderRepository;
import com.example.pingstack.email.repository.EmailConfigRepository;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            BrevoDomain brevoDomain = brevoDomainRepository.findByEmailConfig(emailConfig).orElseThrow(() -> new ResourceNotFoundException("Domain not found"));
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






}
