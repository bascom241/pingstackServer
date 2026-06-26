package com.example.pingstack.auth.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "refresh")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID refreshId;

    private String token;
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;


    public UUID getRefreshId() {
        return refreshId;
    }

    public void setRefreshId(UUID refreshId) {
        this.refreshId = refreshId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
