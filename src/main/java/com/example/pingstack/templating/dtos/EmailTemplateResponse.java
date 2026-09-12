package com.example.pingstack.templating.dtos;

import com.example.pingstack.templating.enums.EmailTemplateCategory;
import com.example.pingstack.templating.enums.EmailTemplateStatus;

public class EmailTemplateResponse {
    private String templateId;
    private String name;
    private String description;
    private EmailTemplateCategory category;
    private String slug;
    private EmailTemplateStatus status;
    private Integer currentVersion;
    private String createdBy;
    private String createdAt;
    private String updatedAt;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmailTemplateCategory getCategory() {
        return category;
    }

    public void setCategory(EmailTemplateCategory category) {
        this.category = category;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public EmailTemplateStatus getStatus() {
        return status;
    }

    public void setStatus(EmailTemplateStatus status) {
        this.status = status;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EmailTemplateResponse(String templateId, String name, String description, EmailTemplateCategory category, String slug, EmailTemplateStatus status, Integer currentVersion, String createdBy, String createdAt, String updatedAt) {
        this.templateId = templateId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.slug = slug;
        this.status = status;
        this.currentVersion = currentVersion;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
