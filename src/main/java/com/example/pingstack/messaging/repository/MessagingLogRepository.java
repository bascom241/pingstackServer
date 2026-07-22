package com.example.pingstack.messaging.repository;

import com.example.pingstack.messaging.model.MessagingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface MessagingLogRepository extends JpaRepository<MessagingLog , UUID> {
    Optional<MessagingLog> findByApiMessageId(String trackingId);
}
