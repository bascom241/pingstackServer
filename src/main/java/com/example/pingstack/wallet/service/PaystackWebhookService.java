package com.example.pingstack.wallet.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PaystackWebhookService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletTopUpService walletTopUpService;

    public void processIncomingWebhook(String paystackSignatureHeader, String rawJsonPayload){
        if(!isWebhookSignatureValid(paystackSignatureHeader,rawJsonPayload)){
            throw new SecurityException("Tampered or invalid webhook signature rejected.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(rawJsonPayload);
        String event = rootNode.get("event").asString();
        if("charge.success".equals(event)){
            JsonNode dataNode = rootNode.get("data");
            String reference = dataNode.get("reference").asString();
            String customerEmail = dataNode.get("customer").get("email").asString();
            BigDecimal amountInKobo = new BigDecimal(dataNode.get("amount").asString());
            BigDecimal divisor = new BigDecimal("100.0000");
            BigDecimal cleanNairaAmount = amountInKobo.divide(divisor, 4, RoundingMode.HALF_UP);
            User user = userRepository.findByEmail(customerEmail).orElseThrow(() -> new RuntimeException("user linked to webhook event not found"));
            walletTopUpService.creditWalletBalance(user.getUserId(), cleanNairaAmount);
        }
    }

    // to be built during testing
    private boolean isWebhookSignatureValid(String header, String payload) {
        // Inside your real app, compute HMAC SHA512 using your Paystack Secret Key
        // against the raw payload string and match it directly with the header string.
        return true;
    }
}
