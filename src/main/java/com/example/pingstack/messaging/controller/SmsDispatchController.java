package com.example.pingstack.messaging.controller;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.messaging.dtos.SmsRequestDto;
import com.example.pingstack.messaging.dtos.SmsResponseDto;
import com.example.pingstack.messaging.service.SmsDistributionService;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsDispatchController {

    @Autowired
    private SmsDistributionService smsDistributionService;

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;



    @PostMapping("/dispatch")
    public ResponseEntity<SmsResponseDto> dispatchMessages(
            @Valid @RequestBody SmsRequestDto request,
            @RequestHeader("Authorization") String authHeader) {


        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);


        // 1. Hand off the bulk processing entirely to the thread pool async worker pipeline
        smsDistributionService.executeBulkPipeline(
                user.getUserId(),
                request.getSenderId(),
                request.getRecipients(),
                request.getMessageBody(),
                request.isTransactional()
        );

        // 2. Build the tracking payload response metadata envelope
        SmsResponseDto responseBody = new SmsResponseDto(
                "ACCEPTED",
                "Bulk SMS transmission has been queued successfully in the background.",
                request.getRecipients().size()
        );

        // 3. Return HTTP Status 202 (Accepted) - Standard protocol for asynchronous tasks
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(responseBody);
    }
}
