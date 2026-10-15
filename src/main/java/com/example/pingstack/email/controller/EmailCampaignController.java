package com.example.pingstack.email.controller;


import com.example.pingstack.email.dto.CampaignResponse;
import com.example.pingstack.email.dto.CreateEmailCampaignRequest;
import com.example.pingstack.email.dto.CreateEmailCampaignResponse;
import com.example.pingstack.email.service.brevo.BrevoCampaignService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaign")
public class EmailCampaignController {


   private final BrevoCampaignService brevoCampaignService;


        public EmailCampaignController(BrevoCampaignService brevoCampaignService) {
        this.brevoCampaignService = brevoCampaignService;
        }



    @PostMapping
    public ResponseEntity<CreateEmailCampaignResponse> createCampaign (@RequestHeader("Authorization") String authHeader, @Valid @RequestBody CreateEmailCampaignRequest createEmailCampaignRequest){
        System.out.println("Name: " + createEmailCampaignRequest.getName());
        System.out.println("Subject: " + createEmailCampaignRequest.getSubject());
      CreateEmailCampaignResponse data = brevoCampaignService.create(authHeader,createEmailCampaignRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(data);
   }
   @GetMapping
    public ResponseEntity<Page<CampaignResponse>> getCampaigns (@RequestHeader("Authorization") String authHeader,  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Page<CampaignResponse> data = brevoCampaignService.getCampaigns(authHeader, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(data);
   }

}
