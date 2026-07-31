package com.example.pingstack.email.dto;



import java.util.List;

public class CustomDomainResponse {
    private String customDomain;
    private boolean isFullyVerified;
    private List<DnsRecord> records;

    public static class DnsRecord {
        private String type;     // "TXT" or "CNAME"
        private String host;     // "@" or "ps._domainkey"
        private String value;    // Expected value
        private String status;   // "Verified" or "Pending"

        public DnsRecord(String type, String host, String value, String status) {
            this.type = type;
            this.host = host;
            this.value = value;
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public CustomDomainResponse(String customDomain, boolean isFullyVerified, List<DnsRecord> records) {
        this.customDomain = customDomain;
        this.isFullyVerified = isFullyVerified;
        this.records = records;
    }

    public String getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(String customDomain) {
        this.customDomain = customDomain;
    }

    public boolean isFullyVerified() {
        return isFullyVerified;
    }

    public void setFullyVerified(boolean fullyVerified) {
        isFullyVerified = fullyVerified;
    }

    public List<DnsRecord> getRecords() {
        return records;
    }

    public void setRecords(List<DnsRecord> records) {
        this.records = records;
    }
}