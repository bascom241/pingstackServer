package com.example.pingstack.wallet.dto;

public class ChartResponse {

    private String timestamp;
    private Long value;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public ChartResponse(String timestamp, Long value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
