package com.example.pingstack.email.model.domain;


import com.example.pingstack.email.model.EmailConfig;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "email_domains")
public class BrevoDomain {

    @Id
    private String brevoDomainId;
    private String domainName;
    private String domainProvider;
    private String message;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_config_id")
    private EmailConfig emailConfig;
    @OneToMany(
            mappedBy = "brevoDomain",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DnsRecord> dnsRecords = new ArrayList<>();
    private boolean isVerified = false;
    private boolean isAuthenticated = false;
    private boolean isPrimary = false;

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getBrevoDomainId() {
        return brevoDomainId;
    }

    public void setBrevoDomainId(String brevoDomainId) {
        this.brevoDomainId = brevoDomainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainProvider() {
        return domainProvider;
    }

    public void setDomainProvider(String domainProvider) {
        this.domainProvider = domainProvider;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public List<DnsRecord> getDnsRecords() {
        return dnsRecords;
    }

    public void setDnsRecords(List<DnsRecord> dnsRecords) {
        this.dnsRecords = dnsRecords;
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
}
