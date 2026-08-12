package com.example.pingstack.email.dto.brevo;

import java.util.UUID;

public class SingleDomainRequestDto {
    private UUID domainId;

    public UUID getDomainId() {
        return domainId;
    }

    public void setDomainId(UUID domainId) {
        this.domainId = domainId;
    }
}
