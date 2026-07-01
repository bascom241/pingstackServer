package com.example.pingstack.wallet.dto;

public class CreateOrderRequest {

    private String reference;
    private String paymentMethod;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public CreateOrderRequest(String reference, String paymentMethod) {
        this.reference = reference;
        this.paymentMethod = paymentMethod;
    }
}
