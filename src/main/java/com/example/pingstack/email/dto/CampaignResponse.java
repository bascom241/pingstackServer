package com.example.pingstack.email.dto;

public class CampaignResponse {
    private String campaignId;
    private String userId;
    private String templateId;
    private String templateVersionId;
    private String name;
    private String subject;
    private String status;
    private Integer totalRecipients;
    private Integer processedRecipients;
    private Integer failedRecipients;
    private String createdAt;
    private String startedAt;


    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateVersionId() {
        return templateVersionId;
    }

    public void setTemplateVersionId(String templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalRecipients() {
        return totalRecipients;
    }

    public void setTotalRecipients(Integer totalRecipients) {
        this.totalRecipients = totalRecipients;
    }

    public Integer getProcessedRecipients() {
        return processedRecipients;
    }

    public void setProcessedRecipients(Integer processedRecipients) {
        this.processedRecipients = processedRecipients;
    }

    public Integer getFailedRecipients() {
        return failedRecipients;
    }

    public void setFailedRecipients(Integer failedRecipients) {
        this.failedRecipients = failedRecipients;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public CampaignResponse(String campaignId, String userId, String templateId, String templateVersionId, String name, String subject, String status, Integer totalRecipients, Integer processedRecipients, Integer failedRecipients, String createdAt, String startedAt) {
        this.campaignId = campaignId;
        this.userId = userId;
        this.templateId = templateId;
        this.templateVersionId = templateVersionId;
        this.name = name;
        this.subject = subject;
        this.status = status;
        this.totalRecipients = totalRecipients;
        this.processedRecipients = processedRecipients;
        this.failedRecipients = failedRecipients;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
    }
}
