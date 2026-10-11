package com.example.pingstack.templating.model;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.templating.enums.EmailTemplateCategory;
import com.example.pingstack.templating.enums.EmailTemplateStatus;
import com.example.pingstack.templating.enums.TemplateType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "email_templates")
public class EmailTemplate {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID emailTemplateId;

        @NotNull
        @Enumerated(EnumType.STRING)
        private EmailTemplateCategory emailTemplateCategory;

        @NotNull
        private String emailName;

        @NotNull
        @Column(unique = true)
        private String slug;

        @NotNull
        @Enumerated(EnumType.STRING)
        private TemplateType templateType = TemplateType.USER;

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    @NotNull
        private String emailTemplateDescription;

        @NotNull
        @Enumerated(EnumType.STRING)
        private EmailTemplateStatus emailTemplateStatus = EmailTemplateStatus.DRAFT;

        @NotNull
        private Integer currentVersion = 1;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "created_by")
        private User createdBy;

        private LocalDateTime createdAt = LocalDateTime.now();

        private LocalDateTime updatedAt;


        @OneToMany(
                mappedBy = "emailTemplate",
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        private List<EmailTemplateVersion> versions = new ArrayList<>();

    public UUID getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(UUID emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public EmailTemplateCategory getEmailTemplateCategory() {
        return emailTemplateCategory;
    }

    public void setEmailTemplateCategory(EmailTemplateCategory emailTemplateCategory) {
        this.emailTemplateCategory = emailTemplateCategory;
    }

    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmailTemplateDescription() {
        return emailTemplateDescription;
    }

    public void setEmailTemplateDescription(String emailTemplateDescription) {
        this.emailTemplateDescription = emailTemplateDescription;
    }

    public EmailTemplateStatus getEmailTemplateStatus() {
        return emailTemplateStatus;
    }

    public void setEmailTemplateStatus(EmailTemplateStatus emailTemplateStatus) {
        this.emailTemplateStatus = emailTemplateStatus;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<EmailTemplateVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<EmailTemplateVersion> versions) {
        this.versions = versions;
    }
}



