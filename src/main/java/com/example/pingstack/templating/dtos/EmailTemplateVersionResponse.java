package com.example.pingstack.templating.dtos;

import java.util.UUID;

public class EmailTemplateVersionResponse {
    private UUID templateVersionId;
    private UUID templateId;
    private Integer version;
    private String subject;
    private String designJson;
    private String htmlContent;
    private String createdAt;

    public UUID getTemplateVersionId() {
        return templateVersionId;
    }

    public void setTemplateVersionId(UUID templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDesignJson() {
        return designJson;
    }

    public void setDesignJson(String designJson) {
        this.designJson = designJson;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public EmailTemplateVersionResponse(UUID templateVersionId, UUID templateId, Integer version, String subject, String designJson, String htmlContent, String createdAt) {
        this.templateVersionId = templateVersionId;
        this.templateId = templateId;
        this.version = version;
        this.subject = subject;
        this.designJson = designJson;
        this.htmlContent = htmlContent;
        this.createdAt = createdAt;
    }
}
