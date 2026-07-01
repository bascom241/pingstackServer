package com.example.pingstack.compliance.dto;

import com.example.pingstack.compliance.enums.SenderIdStatus;

public class SenderIdRequest {


    private String senderId;
    private String companyName;
    private String useCaseSample;


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


}
