package com.example.pingstack.email.dto;

public class ApikeyResponse {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ApikeyResponse(String apiKey) {
        this.apiKey = apiKey;
    }
}
