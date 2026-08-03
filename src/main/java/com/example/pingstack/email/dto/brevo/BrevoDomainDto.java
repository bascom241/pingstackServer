package com.example.pingstack.email.dto.brevo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class BrevoDomainDto {
    private String id;
    private String domain_name;
    private String domain_provider;
    private String message;
    @JsonProperty("dns_records")
    private Map<String, DnsRecordDto> dns_records;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, DnsRecordDto> getDns_records() {
        return dns_records;
    }

    public void setDns_records(Map<String, DnsRecordDto> dns_records) {
        this.dns_records = dns_records;
    }
}
