package com.example.pingstack.auth.model;


import com.example.pingstack.auth.enums.BusinessType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Table(name = "profile")
@Entity

public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID profileId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;


    @NotNull
    @Enumerated(EnumType.STRING)
    private BusinessType sector;


    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    public BusinessType getSector() {
        return sector;
    }

    public void setSector(BusinessType sector) {
        this.sector = sector;
    }
}
