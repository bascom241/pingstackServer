package com.example.pingstack.compliance.model;


import com.example.pingstack.compliance.enums.KycApproval;
import com.example.pingstack.auth.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Table(name = "kyc")
@Entity
public class Kyc {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID kycId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private KycApproval status = KycApproval.Pending;

    @NotNull
    private String kycImageUrl;

    @NotNull
    private String kycImageType;

    @NotNull
    private String kycImageName;


    public UUID getKycId() {
        return kycId;
    }

    public void setKycId(UUID kycId) {
        this.kycId = kycId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public KycApproval getStatus() {
        return status;
    }

    public void setStatus(KycApproval status) {
        this.status = status;
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
}
