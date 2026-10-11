package com.example.pingstack.email.dto;

import com.example.pingstack.email.enums.CampaignStatus;

import java.util.UUID;

public class CreateEmailCampaignResponse {
    private UUID campaignId;
    private CampaignStatus status;


    public UUID getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(UUID campaignId) {
        this.campaignId = campaignId;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public CreateEmailCampaignResponse(UUID campaignId, CampaignStatus status) {
        this.campaignId = campaignId;
        this.status = status;
    }


}
