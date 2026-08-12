package com.example.pingstack.email.dto.brevo;

import java.util.UUID;

public class VerifyDomainRequest {
    private String domainName;
    private String  domainId;

    public String  getDomainId() {
        return domainId;
    }

    public void setDomainId(String  domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
