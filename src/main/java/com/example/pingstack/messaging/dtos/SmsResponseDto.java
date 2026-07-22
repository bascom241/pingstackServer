package com.example.pingstack.messaging.dtos;

public class SmsResponseDto {
    private String status;
    private String message;
    private int totalRecipients;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalRecipients() {
        return totalRecipients;
    }

    public void setTotalRecipients(int totalRecipients) {
        this.totalRecipients = totalRecipients;
    }

    public SmsResponseDto(String status, String message, int totalRecipients) {
        this.status = status;
        this.message = message;
        this.totalRecipients = totalRecipients;
    }
}
