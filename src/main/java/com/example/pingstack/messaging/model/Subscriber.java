package com.example.pingstack.messaging.model;

import com.example.pingstack.messaging.enums.SubscriberStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriber")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID subscriberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String destinationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audience_list_id", nullable = false)
    private AudienceList audienceList;

    private LocalDateTime createdAt = LocalDateTime.now();


    public UUID getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(UUID subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public SubscriberStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriberStatus status) {
        this.status = status;
    }

    public AudienceList getAudienceList() {
        return audienceList;
    }

    public void setAudienceList(AudienceList audienceList) {
        this.audienceList = audienceList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
