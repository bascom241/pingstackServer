package com.example.pingstack.auth.dto;

import com.example.pingstack.auth.enums.BusinessType;
import com.example.pingstack.auth.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateProfileResponse {
    private UUID profileId;
    private String firstName;
    private String lastName;
    private String email;
    private String kycImageUrl;
    private String kycImageType;
    private String kycImageName;
    private BusinessType sector;


    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

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

    public String getKycImageUrl() {
        return kycImageUrl;
    }

    public void setKycImageUrl(String kycImageUrl) {
        this.kycImageUrl = kycImageUrl;
    }

    public String getKycImageType() {
        return kycImageType;
    }

    public void setKycImageType(String kycImageType) {
        this.kycImageType = kycImageType;
    }

    public String getKycImageName() {
        return kycImageName;
    }

    public void setKycImageName(String kycImageName) {
        this.kycImageName = kycImageName;
    }

    public BusinessType getSector() {
        return sector;
    }

    public void setSector(BusinessType sector) {
        this.sector = sector;
    }

    public CreateProfileResponse(UUID profileId, String firstName, String lastName, String email, BusinessType sector) {
        this.profileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sector = sector;
    }
}
