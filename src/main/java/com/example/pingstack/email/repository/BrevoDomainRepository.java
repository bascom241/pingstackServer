package com.example.pingstack.email.repository;

import com.example.pingstack.email.model.EmailConfig;
import com.example.pingstack.email.model.domain.BrevoDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrevoDomainRepository extends JpaRepository<BrevoDomain, String> {
    List<BrevoDomain> findByEmailConfig(EmailConfig emailConfig);
    Optional<BrevoDomain> findByIsPrimaryTrueAndEmailConfig(EmailConfig emailConfig);
    Optional<BrevoDomain> findOneByEmailConfig(EmailConfig emailConfig);
}
