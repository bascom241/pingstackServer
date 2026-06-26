package com.example.pingstack.auth.model;

import com.example.pingstack.auth.enums.BusinessType;
import com.example.pingstack.auth.enums.CreatorRole;
import com.example.pingstack.auth.enums.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles_bridge", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    )

    private Set<Role> roles = new HashSet<>();

    @Column(nullable = true)
    private String verificationCode;

    @Column(nullable = true)
    private Instant verificationExpiry;

    @Column(nullable = true)
    private String resetPasswordVerificationToken;

    @Column(nullable = true)
    private Instant resetPasswordExpiry;


    @Column(nullable = false )
    private boolean verified = false;

    @Size(min = 13, message = "Phone number must be at least 13 characters")
    @Pattern(
            regexp = "^\\+?[0-9]{13,15}$",
            message = "Invalid phone number"
    )
    @NotNull
    @Column(unique = true)
    private String phoneNumber;

    @NotNull
    private String companyName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BusinessType sector;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreatorRole creatorRole;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Location source;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Instant getVerificationExpiry() {
        return verificationExpiry;
    }

    public void setVerificationExpiry(Instant verificationExpiry) {
        this.verificationExpiry = verificationExpiry;
    }

    public String getResetPasswordVerificationToken() {
        return resetPasswordVerificationToken;
    }

    public void setResetPasswordVerificationToken(String resetPasswordVerificationToken) {
        this.resetPasswordVerificationToken = resetPasswordVerificationToken;
    }

    public Instant getResetPasswordExpiry() {
        return resetPasswordExpiry;
    }

    public void setResetPasswordExpiry(Instant resetPasswordExpiry) {
        this.resetPasswordExpiry = resetPasswordExpiry;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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
}
