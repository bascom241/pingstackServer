package com.example.pingstack.auth.dto;

import com.example.pingstack.auth.enums.BusinessType;
import com.example.pingstack.auth.enums.CreatorRole;
import com.example.pingstack.auth.enums.Location;

public class RegisterResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String role ;
    private String phoneNumber;
    private String companyName;
    private BusinessType sector;
    private CreatorRole creatorRole;
    private Location source;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BusinessType getSector() {
        return sector;
    }

    public void setSector(BusinessType sector) {
        this.sector = sector;
    }

    public CreatorRole getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(CreatorRole creatorRole) {
        this.creatorRole = creatorRole;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public RegisterResponseDto(String firstName, String lastName, String email, String role, String phoneNumber, String companyName, BusinessType sector, CreatorRole creatorRole, Location source) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.sector = sector;
        this.creatorRole = creatorRole;
        this.source = source;
    }
}
