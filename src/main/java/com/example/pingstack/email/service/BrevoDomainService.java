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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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

    public DomainVerificationDto verifyDomain(String authHeader, VerifyDomainRequest verifyDomainRequest) {
        if (verifyDomainRequest == null || verifyDomainRequest.getDomainName() == null || verifyDomainRequest.getDomainName().isBlank()) {
            throw new IllegalArgumentException("Domain name must not be empty or null");
        }

        if (verifyDomainRequest.getDomainId() == null) {
            throw new IllegalArgumentException("Domain ID must not be null");
        }

        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("Email config not found for user ID: " + user.getUserId()));

        BrevoDomain brevoDomain = brevoDomainRepository.findById(verifyDomainRequest.getDomainId())
                .orElseThrow(() -> new IllegalStateException("Domain not found with ID: " + verifyDomainRequest.getDomainId()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key", brevoApiKey);
        HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

        String url = BREVO_DOMAINS_URL + "/{domainName}/verify";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<DomainVerificationDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    DomainVerificationDto.class,
                    verifyDomainRequest.getDomainName()
            );

            DomainVerificationDto verificationResult = response.getBody();
            if (verificationResult == null) {
                throw new IllegalStateException("Received empty response from Brevo API during domain verification");
            }

            List<DnsRecord> dnsRecords = dnsProviderRepository.findByBrevoDomain(brevoDomain)
                    .orElseThrow(() -> new IllegalStateException("DNS records configuration not found for domain: " + brevoDomain.getDomainName()));

            Map<String, DnsRecordDto> brevoRecords = verificationResult.getDns_records();
            boolean allRecordsVerified = true;

            if (brevoRecords != null && !brevoRecords.isEmpty()) {
                for (DnsRecord dnsRecord : dnsRecords) {
                    // Match by record type to prevent key-mismatch false positives
                    DnsRecordDto matchingBrevoRecord = brevoRecords.values().stream()
                            .filter(dto -> dto.getType() != null && dto.getType().equalsIgnoreCase(dnsRecord.getType()))
                            .findFirst()
                            .orElse(brevoRecords.get(dnsRecord.getRecordName()));

                    if (matchingBrevoRecord != null) {
                        boolean isVerified = Boolean.TRUE.equals(matchingBrevoRecord.isStatus());
                        dnsRecord.setVerified(isVerified);
                        if (!isVerified) {
                            allRecordsVerified = false;
                        }
                    } else {
                        dnsRecord.setVerified(false);
                        allRecordsVerified = false;
                    }
                }
            } else {
                allRecordsVerified = false;
            }

            dnsProviderRepository.saveAll(dnsRecords);

            // Compute strict verification state
            boolean isDomainFullyVerified = allRecordsVerified && Boolean.TRUE.equals(verificationResult.isVerified());

            brevoDomain.setVerified(isDomainFullyVerified);
            brevoDomain.setAuthenticated(Boolean.TRUE.equals(verificationResult.isAuthenticated()) && isDomainFullyVerified);
            brevoDomainRepository.save(brevoDomain);

            // Sync DTO before returning to client
            verificationResult.setVerified(isDomainFullyVerified);

            return verificationResult;

        } catch (HttpStatusCodeException e) {
            String responseBody = e.getResponseBodyAsString();

            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new IllegalStateException("Invalid Brevo API key or credentials");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new IllegalArgumentException("Domain '" + verifyDomainRequest.getDomainName() + "' was not found in Brevo");
            }

            throw new RuntimeException("Brevo API error: " + extractBrevoMessage(responseBody), e);

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Re-throw standard validation or state errors directly
            throw e;

        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while verifying domain: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts human-readable message from Brevo error JSON body.
     */
    private String extractBrevoMessage(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            if (root.has("message")) {
                return root.get("message").asText();
            }
        } catch (Exception ignored) {
            // Fallback if parsing fails
        }
        return "Failed to complete DNS verification with external provider";
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



    @Transactional
    public String deleteDomain(String domainId, String authHeader) {
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);

        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Email config not found for the user"));

        BrevoDomain brevoDomain = brevoDomainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found"));

        if (!brevoDomain.getEmailConfig().getUserId().equals(emailConfig.getUserId())) {
            throw new AccessDeniedException("You cannot delete this domain");
        }

        String domainName = brevoDomain.getDomainName();

        // 1. Delete domain from Brevo API
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("api-key", brevoApiKey);
            HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

            String url = BREVO_DOMAINS_URL + "/" + domainName;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);

        } catch (HttpStatusCodeException e) {
            // If Brevo returns 404, the domain is already removed on Brevo, so we proceed silently.
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Failed to delete domain from Brevo: " + e.getResponseBodyAsString(), e);
            }
        }

        // 2. Delete linked DNS records locally (if cascading delete is not configured on JPA relationship)
        dnsProviderRepository.deleteByBrevoDomain(brevoDomain);

        // 3. Delete domain locally
        brevoDomainRepository.delete(brevoDomain);

        return domainName + " deleted successfully";
    }







}
