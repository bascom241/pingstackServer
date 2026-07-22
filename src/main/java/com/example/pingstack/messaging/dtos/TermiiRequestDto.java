package com.example.pingstack.messaging.dtos;

import com.example.pingstack.messaging.enums.MessageChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public class TermiiRequestDto {

    @JsonProperty("api_key")
    private String apiKey;
    @JsonProperty("to")
    private List<String> to;
    @JsonProperty("from")
    private String from;
    @JsonProperty("sms")
    private String sms;
    @JsonProperty("type")
    private String type = "plain";
    @JsonProperty("channel")
    @Enumerated(EnumType.STRING)
    private MessageChannel channel;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public TermiiRequestDto(String apiKey, List<String> to, String from, String sms, String type, MessageChannel channel) {
        this.apiKey = apiKey;
        this.to = to;
        this.from = from;
        this.sms = sms;
        this.type = (type != null) ? type : "plain";
        this.channel = channel;
    }
}
