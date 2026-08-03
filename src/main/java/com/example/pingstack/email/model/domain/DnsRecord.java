package com.example.pingstack.email.model.domain;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dns_records")
public class DnsRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID dnsRecordId;
    private String recordName;
    private String type;
    private String hostName;
    @Column(columnDefinition = "TEXT")
    private String value;
    private boolean verified;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private BrevoDomain brevoDomain;

    public UUID getDnsRecordId() {
        return dnsRecordId;
    }

    public void setDnsRecordId(UUID dnsRecordId) {
        this.dnsRecordId = dnsRecordId;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public BrevoDomain getBrevoDomain() {
        return brevoDomain;
    }

    public void setBrevoDomain(BrevoDomain brevoDomain) {
        this.brevoDomain = brevoDomain;
    }
}
