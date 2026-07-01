package com.example.pingstack.compliance.dto;

import com.example.pingstack.compliance.enums.KycApproval;

import java.util.UUID;

public class KycUploadResponse {
    private UUID kycId;
    private String uploadedBy;
    private KycApproval status;
    private String kycImageUrl;
    private String kycImageType;
    private String kycImageName;


    public UUID getKycId() {
        return kycId;
    }

    public void setKycId(UUID kycId) {
        this.kycId = kycId;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public KycApproval getStatus() {
        return status;
    }

    public void setStatus(KycApproval status) {
        this.status = status;
    }

    public String getKycImageUrl() {
        return kycImageUrl;
    }

    public void setKycImageUrl(String kycImageUrl) {
        this.kycImageUrl = kycImageUrl;
    }

    public String getKycImageType() {
        return kycImageType;
    }

    public void setKycImageType(String kycImageType) {
        this.kycImageType = kycImageType;
    }

    public String getKycImageName() {
        return kycImageName;
    }

    public void setKycImageName(String kycImageName) {
        this.kycImageName = kycImageName;
    }

    public KycUploadResponse(UUID kycId, String uploadedBy, KycApproval status, String kycImageUrl, String kycImageType, String kycImageName) {
        this.kycId = kycId;
        this.uploadedBy = uploadedBy;
        this.status = status;
        this.kycImageUrl = kycImageUrl;
        this.kycImageType = kycImageType;
        this.kycImageName = kycImageName;
    }
}
