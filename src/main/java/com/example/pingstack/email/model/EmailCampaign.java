package com.example.pingstack.email.model;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.enums.CampaignStatus;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_campaign")
public class EmailCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @ManyToOne
    private User user;


    @ManyToOne
    private EmailTemplate template;


    @ManyToOne
    private EmailTemplateVersion templateVersion;

    @NotNull
    private String name;

    @NotNull
    private String subject;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    private Integer totalRecipients;



    private Integer processedRecipients;


    private Integer successfulRecipients;


    private Integer failedRecipients;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;





    // Getters and Setters


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EmailTemplate template) {
        this.template = template;
    }

    public EmailTemplateVersion getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(EmailTemplateVersion templateVersion) {
        this.templateVersion = templateVersion;
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

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
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

    public Integer getSuccessfulRecipients() {
        return successfulRecipients;
    }

    public void setSuccessfulRecipients(Integer successfulRecipients) {
        this.successfulRecipients = successfulRecipients;
    }

    public Integer getFailedRecipients() {
        return failedRecipients;
    }

    public void setFailedRecipients(Integer failedRecipients) {
        this.failedRecipients = failedRecipients;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
