package com.example.pingstack.email.dto;

import java.util.List;
import java.util.UUID;

public class CreateEmailCampaignRequest {
    private String name;
    private String  templateId;
    private String subject;
    private List<EmailRecipientRequest> recipientRequests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<EmailRecipientRequest> getRecipientRequests() {
        return recipientRequests;
    }

    public void setRecipientRequests(List<EmailRecipientRequest> recipientRequests) {
        this.recipientRequests = recipientRequests;
    }
}
