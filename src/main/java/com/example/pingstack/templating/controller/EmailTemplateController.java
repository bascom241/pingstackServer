package com.example.pingstack.templating.controller;


import com.example.pingstack.templating.dtos.CreateTemplateRequestDto;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.dtos.EmailTemplateVersionRequest;
import com.example.pingstack.templating.dtos.EmailTemplateVersionResponse;
import com.example.pingstack.templating.service.EmailTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email-template")
public class EmailTemplateController {

    @Autowired
    private EmailTemplateService emailTemplateService;


    @PostMapping
    public ResponseEntity<EmailTemplateResponse> create (@RequestHeader("Authorization") String authHeader, @Valid  @RequestBody CreateTemplateRequestDto createTemplateRequestDto){
        EmailTemplateResponse data = emailTemplateService.createTemplate(authHeader, createTemplateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @GetMapping
    public ResponseEntity<List<EmailTemplateResponse>> getAllForUser(@RequestHeader("Authorization") String authHeader){
        List<EmailTemplateResponse> data = emailTemplateService.getAllEmailTemplateForUser(authHeader);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<EmailTemplateVersionResponse> editTemplateVersion (@RequestHeader("Authorization") String authHeader , @RequestBody EmailTemplateVersionRequest emailTemplateVersionRequest, @PathVariable UUID templateId){
        EmailTemplateVersionResponse data = emailTemplateService.editTemplateVersion(templateId,authHeader,emailTemplateVersionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<EmailTemplateVersionResponse> getTemplateVersion (@RequestHeader("Authorization") String authHeader , @PathVariable UUID templateId){
        EmailTemplateVersionResponse data = emailTemplateService.getSingleEmailTemplate(templateId, authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }



}
