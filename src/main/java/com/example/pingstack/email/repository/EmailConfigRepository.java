package com.example.pingstack.email.repository;

import com.example.pingstack.email.model.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailConfigRepository extends JpaRepository<EmailConfig, UUID> {
    Optional<EmailConfig> findByUserId(UUID userId);
}
