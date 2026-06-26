package com.example.pingstack.wallet.service;


import com.example.pingstack.wallet.dto.WalletBalanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PayStackService {


    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paystack.secret.key}")
    private String SECRET_KEY;

    @Value("${paystack.init.url}")
    private String PAYSTACK_INIT_URL;

    @Value("${paystack.callback.url}")
    private String callBackUrl;


    public  String initializePayment(UUID userId, BigDecimal amountInNaira, String userEmail){
        String paymentReference = "PING_" + UUID.randomUUID().toString().replace("-","");
        BigDecimal amountInKobo = amountInNaira.multiply(new BigDecimal(100));
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", userEmail);
        payload.put("amount", amountInKobo.longValue());
        payload.put("reference", paymentReference);
        payload.put("callback_url", callBackUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(SECRET_KEY);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload,httpHeaders);
        ResponseEntity<Map> response = restTemplate.postForEntity(PAYSTACK_INIT_URL,entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        return data.get("authorization_url").toString();
    }







}
