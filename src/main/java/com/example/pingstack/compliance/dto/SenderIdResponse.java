package com.example.pingstack.compliance.dto;

import com.example.pingstack.compliance.enums.SenderIdStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class SenderIdResponse {

    private String senderId;
    private String companyName;
    private String useCaseSample;
    private SenderIdStatus status;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUseCaseSample() {
        return useCaseSample;
    }

    public void setUseCaseSample(String useCaseSample) {
        this.useCaseSample = useCaseSample;
    }

    public SenderIdStatus getStatus() {
        return status;
    }

    public void setStatus(SenderIdStatus status) {
        this.status = status;
    }

    public SenderIdResponse(String senderId, String companyName, String useCaseSample, SenderIdStatus status) {
        this.senderId = senderId;
        this.companyName = companyName;
        this.useCaseSample = useCaseSample;
        this.status = status;
    }
}
