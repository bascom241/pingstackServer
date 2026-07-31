package com.example.pingstack.messaging.dtos;

import com.example.pingstack.messaging.enums.SubscriberStatus;

import java.util.UUID;

public class UpdateSubscriberRequest {


    private String name;
    private String destinationNumber;
    private SubscriberStatus status;
    private UUID listId;
    private UUID subscriberId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public SubscriberStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriberStatus status) {
        this.status = status;
    }

    public UUID getListId() {
        return listId;
    }

    public void setListId(UUID listId) {
        this.listId = listId;
    }

    public UUID getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(UUID subscriberId) {
        this.subscriberId = subscriberId;
    }
}
