package com.example.pingstack.messaging.controller;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.messaging.dtos.*;
import com.example.pingstack.messaging.service.AudienceService;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audience")
public class AudienceController {

    @Autowired
    private AudienceService audienceService;

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;


    @PostMapping
    public ResponseEntity<AudienceResponse> createAudience (@RequestHeader("Authorization") String authHeader, @RequestBody CreateAudienceRequest createAudienceRequest){
        AudienceResponse data = audienceService.createAudienceList(authHeader, createAudienceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }



    @GetMapping
    public ResponseEntity<List<AudienceListSummary>> getAllAudiences (@RequestHeader ("Authorization") String authHeader){
        List<AudienceListSummary> data = audienceService.getAllAudienceList(authHeader);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/{listId}")
    public ResponseEntity<AudienceResponse> getSingleAudience(@PathVariable UUID listId) {
        AudienceResponse data = audienceService.getSingleAudience(listId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/subscribers")
    public ResponseEntity<Page<CreateSubscriberResponse>> getSubscribers ( @RequestHeader("Authorization") String authHeader, @RequestParam(defaultValue = "") String query , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam UUID listId){
        Page<CreateSubscriberResponse> data = audienceService.getPaginatedSubscribers(listId, query, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/subscriber")
    public ResponseEntity<CreateSubscriberResponse> addSubscriber (@RequestHeader("Authorization") String authHeader, @RequestBody CreateSubscriberRequest createSubscriberRequest){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        CreateSubscriberResponse data = audienceService.addSingleMember(createSubscriberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadSubscribersCsv(@RequestParam("file") MultipartFile file , @RequestParam UUID listId){
        Integer data = audienceService.uploadBulkSubscribers(listId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

}
