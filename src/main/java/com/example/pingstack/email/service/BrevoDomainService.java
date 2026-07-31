package com.example.pingstack.email.service;


import com.example.pingstack.email.dto.UpdateDomainRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class BrevoDomainService {

    @Value("${brevo.api.key}")
    private String brevoApiKey;


    private static final String BREVO_DOMAINS_URL = "https://api.brevo.com/v3/senders/domains";


    public String registerDomain(UpdateDomainRequest updateDomainRequest){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key", brevoApiKey);

        Map<String, Object> body = Map.of("name", updateDomainRequest.getCustomDomain());
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_DOMAINS_URL, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
