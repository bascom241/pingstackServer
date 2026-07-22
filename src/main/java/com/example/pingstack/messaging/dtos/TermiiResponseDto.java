package com.example.pingstack.messaging.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TermiiResponseDto {

    private String code;
    private BigDecimal balance;
    @JsonProperty("message_id")
    private String messageId;
    private String message;
    private String user;
    @JsonProperty("message_id_str")
    private String messageIdStr;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessageIdStr() {
        return messageIdStr;
    }

    public void setMessageIdStr(String messageIdStr) {
        this.messageIdStr = messageIdStr;
    }

    public TermiiResponseDto(String code, BigDecimal balance, String messageId, String message, String user, String messageIdStr) {
        this.code = code;
        this.balance = balance;
        this.messageId = messageId;
        this.message = message;
        this.user = user;
        this.messageIdStr = messageIdStr;
    }
}
