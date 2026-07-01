package com.example.pingstack.auth.dto;

import com.example.pingstack.auth.enums.BusinessType;
import com.example.pingstack.auth.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateProfileRequest {

    private String firstName;
    private String lastName;
    private String email;
    private BusinessType sector;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BusinessType getSector() {
        return sector;
    }

    public void setSector(BusinessType sector) {
        this.sector = sector;
    }
}
