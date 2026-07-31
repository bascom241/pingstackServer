package com.example.pingstack.messaging.dtos;

import java.util.UUID;

public class DeleteSubscriberRequest {
    private UUID subscriberId;
    private String subscriberName;
    private UUID audienceId;
    public UUID getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(UUID subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public UUID getAudienceId() {
        return audienceId;
    }

    public void setAudienceId(UUID audienceId) {
        this.audienceId = audienceId;
    }
}
