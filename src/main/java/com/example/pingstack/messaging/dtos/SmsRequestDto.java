package com.example.pingstack.messaging.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SmsRequestDto {
    @NotBlank(message = "Sender ID is mandatory")
    @Size(max = 11, message = "Alphanumeric Sender ID cannot exceed 11 characters")
    private String senderId;

    @NotEmpty(message = "Recipients list cannot be empty")
    private List<String> recipients;

    @NotBlank(message = "Message body cannot be blank")
    private String messageBody;

    private boolean isTransactional;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public boolean isTransactional() {
        return isTransactional;
    }

    public void setTransactional(boolean transactional) {
        isTransactional = transactional;
    }
}
