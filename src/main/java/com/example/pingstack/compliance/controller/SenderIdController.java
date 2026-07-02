package com.example.pingstack.compliance.controller;

import com.example.pingstack.compliance.dto.SenderIdRequest;
import com.example.pingstack.compliance.dto.SenderIdResponse;
import com.example.pingstack.compliance.service.SenderIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/senderId")
public class SenderIdController {


    @Autowired
    private SenderIdService senderIdService;

    @PostMapping
    public ResponseEntity<SenderIdResponse> createSenderId(@RequestHeader("Authorization") String authHeader, @RequestBody SenderIdRequest senderIdRequest){
        SenderIdResponse data = senderIdService.registerSenderId(authHeader, senderIdRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
}
