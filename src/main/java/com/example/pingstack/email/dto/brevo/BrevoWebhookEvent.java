package com.example.pingstack.email.dto.brevo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BrevoWebhookEvent {

    private String event;
    private String email;

    @JsonProperty("message-id")
    private String messageId;

    @JsonProperty("ts_event")
    private Long eventTimestamp;

    private String reason;
    private String link;


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
