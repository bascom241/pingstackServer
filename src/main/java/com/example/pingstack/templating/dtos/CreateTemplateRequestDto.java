package com.example.pingstack.templating.dtos;

import com.example.pingstack.templating.enums.EmailTemplateCategory;
import com.example.pingstack.templating.enums.EmailTemplateStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateTemplateRequestDto {
    private String name;
    private String description;
    private EmailTemplateCategory category;
    private String slug;
    private EmailTemplateStatus status;
    private String currentVersion;

    @NotNull
    @Valid
    private CreateEmailTemplateVersionRequest version;

    public CreateEmailTemplateVersionRequest getVersion() {
        return version;
    }

    public void setVersion(CreateEmailTemplateVersionRequest version) {
        this.version = version;
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

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
}
