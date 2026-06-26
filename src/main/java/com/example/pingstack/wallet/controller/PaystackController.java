package com.example.pingstack.wallet.controller;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.wallet.dto.WalletTopUpRequestDto;
import com.example.pingstack.wallet.service.PayStackService;
import com.example.pingstack.wallet.service.PaystackWebhookService;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaystackController {

    @Autowired
    private PaystackWebhookService paystackWebhookService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private PayStackService payStackService;

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeWalletTopUpPayment (@RequestHeader("Authorization") String authHeader , @RequestBody WalletTopUpRequestDto walletTopUpRequestDto){

            User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
            String url = payStackService.initializePayment(user.getUserId(),walletTopUpRequestDto.getAmount(), user.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(url);

    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaystackWebhook(
            @RequestHeader("x-paystack-signature") String signature,
            @RequestBody String rawJsonPayLoad
    ){
        try {
            paystackWebhookService.processIncomingWebhook(signature, rawJsonPayLoad);
            return ResponseEntity.status(HttpStatus.OK).body("Event Processed successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }catch (Exception e ){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
