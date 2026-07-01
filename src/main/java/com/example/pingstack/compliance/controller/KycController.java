package com.example.pingstack.compliance.controller;


import com.example.pingstack.compliance.dto.KycUploadResponse;
import com.example.pingstack.compliance.service.KycDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/kyc")
public class KycController {

    @Autowired
    private KycDocumentService kycDocumentService;


    @PostMapping
    public ResponseEntity<KycUploadResponse> uploadKyc (@RequestHeader("Authorization") String authHeader, @RequestPart MultipartFile file){
        KycUploadResponse data = kycDocumentService.uploadKyc(authHeader,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
}
