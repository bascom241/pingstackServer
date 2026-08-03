package com.example.pingstack.email.dto.brevo;
public class DnsRecordDto {
    private String recordName;
    private String type;
    private String value;
    private String host_name;
    private boolean status;


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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DnsRecordDto() {
    }

    public DnsRecordDto(String recordName, String type, String value, String host_name, boolean status) {
        this.recordName = recordName;
        this.type = type;
        this.value = value;
        this.host_name = host_name;
        this.status = status;
    }
}
