package com.example.pingstack.email.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_config")
public class EmailConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID EmailConfig;

    @Column(name = "userId" , nullable = false)
    private UUID userId;

    // Stores SHA-256 hash of the API key
    @Column(name = "api_key_hash", nullable = false, unique = true)
    private String apiKeyHash;

    // Stores truncated version to display in dashboard (e.g. "ps_live_8f93a0d1...")
    @Column(name = "api_key_prefix", nullable = false)
    private String apiKeyPrefix;

    @Column(name = "custom_domain")
    private String customDomain;

    @Column(name = "track_opens", nullable = false)
    private boolean trackOpens = true;

    @Column(name = "track_clicks", nullable = false)
    private boolean trackClicks = true;

    @Column(name = "bounce_alerts", nullable = false)
    private boolean bounceAlerts = true;

    @Column(name = "emails_sent_this_month", nullable = false)
    private int emailsSentThisMonth  = 0;

    @Column(name = "monthly_limit", nullable = false)
    private int monthlyLimit = 10000;

    @Column(name = "spf_verified", nullable = false)
    private boolean spfVerified = false;

    @Column(name = "dkim_verified", nullable = false)
    private boolean dkimVerified = false;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PreUpdate
    @PrePersist
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getEmailConfig() {
        return EmailConfig;
    }

    public void setEmailConfig(UUID emailConfig) {
        EmailConfig = emailConfig;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

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


    public boolean isSpfVerified() {
        return spfVerified;
    }

    public void setSpfVerified(boolean spfVerified) {
        this.spfVerified = spfVerified;
    }

    public boolean isDkimVerified() {
        return dkimVerified;
    }

    public void setDkimVerified(boolean dkimVerified) {
        this.dkimVerified = dkimVerified;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
