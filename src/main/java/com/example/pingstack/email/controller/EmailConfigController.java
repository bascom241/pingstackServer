package com.example.pingstack.email.controller;

import com.example.pingstack.email.dto.*;
import com.example.pingstack.email.dto.brevo.*;
import com.example.pingstack.email.service.BrevoDomainService;
import com.example.pingstack.email.service.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


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
        System.out.println(updatePreferenceRequest.isBounceAlerts()+ " " + updatePreferenceRequest.isTrackOpens());
        UpdatePreferenceResponse data = emailConfigService.updateUserPreference(authHeader,updatePreferenceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/register")
    public ResponseEntity<BrevoDomainDto> registerOrUpdateDomain(@RequestHeader("Authorization") String authHeader,@RequestBody UpdateDomainRequest updateDomainRequest){
        System.out.println(updateDomainRequest.getCustomDomain());
        BrevoDomainDto data = brevoDomainService.registerDomain(authHeader,updateDomainRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/verify")
    public ResponseEntity<DomainVerificationDto> verifyDomain(@RequestHeader("Authorization") String authHeader, @RequestBody VerifyDomainRequest verifyDomainRequest){
        DomainVerificationDto data = brevoDomainService.verifyDomain(authHeader, verifyDomainRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/domain")
    public ResponseEntity<List<AllBrevoDomainDto>> getAllDomains (@RequestHeader("Authorization") String authHeader) {
        List<AllBrevoDomainDto> data = brevoDomainService.getAllDomainsWithDnsConfig(authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<AllBrevoDomainDto> getDomainInfo (@PathVariable  String  domainId) {
        AllBrevoDomainDto data = brevoDomainService.getSingleDomainDetail(domainId);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    @GetMapping("/update-warning-domain/{domainId}")
    public ResponseEntity<AllBrevoDomainDto> primaryUpdate ( @RequestHeader("Authorization") String authHeader ,  @PathVariable String domainId) {
        AllBrevoDomainDto data = brevoDomainService.warningBeforeMakingPrimary(authHeader, domainId);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PutMapping("/domain/{domainId}")
    public ResponseEntity<AllBrevoDomainDto> updatePrimaryDomain(@RequestHeader("Authorization") String authHeader , @PathVariable String domainId)
    {
        AllBrevoDomainDto data = brevoDomainService.makeDomainPrimary(authHeader, domainId);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @DeleteMapping("/domain/{domainId}")
    public ResponseEntity<String> deleteDomain(@RequestHeader("Authorization") String authHeader, @PathVariable String domainId) {
        String  data = brevoDomainService.deleteDomain(domainId, authHeader);
        return ResponseEntity.status(HttpStatus.GONE).body(data);
    }


}
