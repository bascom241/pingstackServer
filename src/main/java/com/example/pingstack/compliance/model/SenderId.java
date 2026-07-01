package com.example.pingstack.compliance.model;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.compliance.enums.SenderIdStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Table(name = "senderId")
@Entity
public class SenderId {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(length = 11)
    private String senderId;

    @NotNull
    private String companyName;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String useCaseSample;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SenderIdStatus status = SenderIdStatus.Pending;

    @NotNull
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUseCaseSample() {
        return useCaseSample;
    }

    public void setUseCaseSample(String useCaseSample) {
        this.useCaseSample = useCaseSample;
    }

    public SenderIdStatus getStatus() {
        return status;
    }

    public void setStatus(SenderIdStatus status) {
        this.status = status;
    }
}
