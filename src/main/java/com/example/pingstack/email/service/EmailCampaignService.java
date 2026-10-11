package com.example.pingstack.email.service;

import com.example.pingstack.email.dto.CreateEmailCampaignRequest;
import com.example.pingstack.email.dto.CreateEmailCampaignResponse;
import org.springframework.stereotype.Service;


public interface EmailCampaignService {
    CreateEmailCampaignResponse create(String authHeader, CreateEmailCampaignRequest createEmailCampaignRequest);
}
