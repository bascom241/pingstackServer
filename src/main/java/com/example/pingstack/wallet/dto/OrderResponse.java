package com.example.pingstack.wallet.dto;
import java.time.LocalDateTime;
import java.util.UUID;
public class OrderResponse {
    private UUID orderId;
    private String orderCreator;
    private String transactionNumber;
    private String paymentMethod;
    private LocalDateTime createdAt;


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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderResponse(UUID orderId, String orderCreator, String transactionNumber, String paymentMethod, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderCreator = orderCreator;
        this.transactionNumber = transactionNumber;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }
}
