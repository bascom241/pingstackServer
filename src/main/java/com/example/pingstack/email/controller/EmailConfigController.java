package com.example.pingstack.email.controller;

import com.example.pingstack.email.dto.*;
import com.example.pingstack.email.service.BrevoDomainService;
import com.example.pingstack.email.service.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/email-config")
@RestController
public class EmailConfigController {


    @Autowired
    private EmailConfigService emailConfigService;


    @Autowired
    private BrevoDomainService brevoDomainService;
    // Tested
    @GetMapping
    public ResponseEntity<EmailConfigResponse> getEmailConfig(@RequestHeader("Authorization") String authHeader){
        EmailConfigResponse emailConfigResponse = emailConfigService.createOrGetEmailConfig(authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(emailConfigResponse);
    }

    // Tested
    @PostMapping
    public ResponseEntity<ApiKeyGeneratedResponse> regenerateApiKey(@RequestHeader("Authorization") String authHeader){
        ApiKeyGeneratedResponse data = emailConfigService.regenerateKey(authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PutMapping("/preference")
    public ResponseEntity<UpdatePreferenceResponse> updatePref (@RequestHeader("Authorization") String authHeader, @RequestBody UpdatePreferenceRequest updatePreferenceRequest){
        UpdatePreferenceResponse data = emailConfigService.updateUserPreference(authHeader,updatePreferenceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerOrUpdateDomain(@RequestBody UpdateDomainRequest updateDomainRequest){
        String data = brevoDomainService.registerDomain(updateDomainRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
