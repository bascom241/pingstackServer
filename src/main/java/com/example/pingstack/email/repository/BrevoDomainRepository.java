package com.example.pingstack.email.repository;

import com.example.pingstack.email.model.EmailConfig;
import com.example.pingstack.email.model.domain.BrevoDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrevoDomainRepository extends JpaRepository<BrevoDomain, UUID> {
    Optional<BrevoDomain> findByEmailConfig(EmailConfig emailConfig);

}
