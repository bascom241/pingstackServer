package com.example.pingstack.email.dto;

import jakarta.validation.constraints.NotNull;

public class EmailRecipientRequest {


    private String email;
    private String name;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
