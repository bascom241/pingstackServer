package com.example.pingstack.ErrorMessage;

import java.time.LocalTime;

public class ErrorMessage {
    private int status;
    private String message;
    private LocalTime localTime;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public ErrorMessage(int status, String message, LocalTime localTime) {
        this.status = status;
        this.message = message;
        this.localTime = localTime;
    }

}
