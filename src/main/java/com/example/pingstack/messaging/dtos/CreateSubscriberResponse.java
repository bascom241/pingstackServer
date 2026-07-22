package com.example.pingstack.messaging.dtos;

import com.example.pingstack.messaging.enums.SubscriberStatus;
import com.example.pingstack.messaging.model.AudienceList;

import java.util.UUID;

public class CreateSubscriberResponse {
    private UUID orderId; // mapped to match your existing frontend setup
    private String orderCreator; // maps to Subscriber Name
    private String transactionNumber; // maps to Destination Number
    private SubscriberStatus status;
    private String createdAt;

    public CreateSubscriberResponse(UUID orderId, String orderCreator, String transactionNumber, SubscriberStatus status, String createdAt) {
        this.orderId = orderId;
        this.orderCreator = orderCreator;
        this.transactionNumber = transactionNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getOrderCreator() {
        return orderCreator;
    }

    public void setOrderCreator(String orderCreator) {
        this.orderCreator = orderCreator;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public SubscriberStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriberStatus status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
