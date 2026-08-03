package com.example.pingstack.email.dto;

import jakarta.persistence.Column;

public class EmailConfigResponse {

    private String apiKeyHash;
    private String apiKeyPrefix;
    private String customDomain;
    private boolean trackOpens;
    private boolean trackClicks;
    private boolean bounceAlerts;
    private int emailsSentThisMonth;
    private int monthlyLimit;

    public String getApiKeyHash() {
        return apiKeyHash;
    }

    public void setApiKeyHash(String apiKeyHash) {
        this.apiKeyHash = apiKeyHash;
    }

    public String getApiKeyPrefix() {
        return apiKeyPrefix;
    }

    public void setApiKeyPrefix(String apiKeyPrefix) {
        this.apiKeyPrefix = apiKeyPrefix;
    }

    public String getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(String customDomain) {
        this.customDomain = customDomain;
    }

    public boolean isTrackOpens() {
        return trackOpens;
    }

    public void setTrackOpens(boolean trackOpens) {
        this.trackOpens = trackOpens;
    }

    public boolean isTrackClicks() {
        return trackClicks;
    }

    public void setTrackClicks(boolean trackClicks) {
        this.trackClicks = trackClicks;
    }

    public boolean isBounceAlerts() {
        return bounceAlerts;
    }

    public void setBounceAlerts(boolean bounceAlerts) {
        this.bounceAlerts = bounceAlerts;
    }

    public int getEmailsSentThisMonth() {
        return emailsSentThisMonth;
    }

    public void setEmailsSentThisMonth(int emailsSentThisMonth) {
        this.emailsSentThisMonth = emailsSentThisMonth;
    }

    public int getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(int monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public EmailConfigResponse(String apiKeyHash, String apiKeyPrefix, String customDomain, boolean trackOpens, boolean trackClicks, boolean bounceAlerts, int emailsSentThisMonth, int monthlyLimit) {
        this.apiKeyHash = apiKeyHash;
        this.apiKeyPrefix = apiKeyPrefix;
        this.customDomain = customDomain;
        this.trackOpens = trackOpens;
        this.trackClicks = trackClicks;
        this.bounceAlerts = bounceAlerts;
        this.emailsSentThisMonth = emailsSentThisMonth;
        this.monthlyLimit = monthlyLimit;
    }
}
