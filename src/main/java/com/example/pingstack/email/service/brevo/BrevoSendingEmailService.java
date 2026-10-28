package com.example.pingstack.email.service.brevo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;


@Service
public class BrevoSendingEmailService {

    private static final String BREVO_SEND_EMAIL_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendEmail(
            String recipientName,
            String recipientEmail,
            String subject,
            String htmlContent
    ){
        HttpHeaders httpHeaders = new HttpHeaders();

        System.out.println(brevoApiKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("api-key", brevoApiKey);

        Map<String, Object> sender = Map.of(
                "email", senderEmail,
                "name", senderName
        );

        Map<String, Object> recipient = Map.of(
          "email", recipientEmail, "name", recipientName
        );

        Map<String, Object> requestBody = Map.of(
          "sender", sender,
          "to", List.of(recipient),
                "subject", subject,
                "htmlContent", htmlContent

        );

        HttpEntity<Map<String, Object >> request = new HttpEntity<>(requestBody, httpHeaders);


        ResponseEntity<Map> response = restTemplate.postForEntity(BREVO_SEND_EMAIL_URL, request, Map.class);

        Map responseBody = response.getBody();

        if(responseBody == null || responseBody.get("messageId") == null){
            throw new IllegalStateException("Brevo received no identifiable message response");
        }

        return responseBody.get("messageId").toString();
    }


}
