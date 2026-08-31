package com.example.pingstack.templating.controller;


import com.example.pingstack.templating.dtos.CreateTemplateRequestDto;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.service.EmailTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
