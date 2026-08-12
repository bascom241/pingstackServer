package com.example.pingstack.email.dto.brevo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AllBrevoDomainDto {

    private String id;
    private String domain_name;
    private String domain_provider;
    private boolean isVerified;
    private boolean isAuthenticated;
    @JsonProperty("dns_records")
    private Map<String, DnsRecordDto> dns_records;
    private boolean isPrimary;

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getDomain_provider() {
        return domain_provider;
    }

    public void setDomain_provider(String domain_provider) {
        this.domain_provider = domain_provider;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public Map<String, DnsRecordDto> getDns_records() {
        return dns_records;
    }

    public void setDns_records(Map<String, DnsRecordDto> dns_records) {
        this.dns_records = dns_records;
    }


    public AllBrevoDomainDto(String id, String domain_name, String domain_provider, boolean isVerified, boolean isAuthenticated, Map<String, DnsRecordDto> dns_records, boolean isPrimary) {
        this.id = id;
        this.domain_name = domain_name;
        this.domain_provider = domain_provider;
        this.isVerified = isVerified;
        this.isAuthenticated = isAuthenticated;
        this.dns_records = dns_records;
        this.isPrimary = isPrimary;
    }
}
