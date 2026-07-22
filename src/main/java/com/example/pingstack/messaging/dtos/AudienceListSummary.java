package com.example.pingstack.messaging.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AudienceListSummary {
    UUID getId();
    String getName();
    LocalDateTime getUpdatedAt();
    Long getTotalCount();
}
