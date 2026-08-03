package com.example.pingstack.email.dto.brevo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DomainVerificationDto {

    private String domain;
    private boolean verified;
    private boolean authenticated;
    @JsonProperty("dns_records")
    private Map<String, DnsRecordDto> dns_records;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Map<String, DnsRecordDto> getDns_records() {
        return dns_records;
    }

    public void setDns_records(Map<String, DnsRecordDto> dns_records) {
        this.dns_records = dns_records;
    }
}
