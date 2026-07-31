package com.example.pingstack.email.dto;

public class ApiKeyGeneratedResponse {

    private String rawApiKey;
    private String apiKeyPrefix;
    private String message;


    public ApiKeyGeneratedResponse(String rawApiKey, String apiKeyPrefix) {
        this.rawApiKey = rawApiKey;
        this.apiKeyPrefix = apiKeyPrefix;
        this.message = "Save this API key. You will not be able to view it again!";
    }

    public String getRawApiKey() {
        return rawApiKey;
    }

    public void setRawApiKey(String rawApiKey) {
        this.rawApiKey = rawApiKey;
    }

    public String getApiKeyPrefix() {
        return apiKeyPrefix;
    }

    public void setApiKeyPrefix(String apiKeyPrefix) {
        this.apiKeyPrefix = apiKeyPrefix;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
