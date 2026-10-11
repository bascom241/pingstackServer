package com.example.pingstack.email.repository;

import com.example.pingstack.email.model.EmailRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailRecipientRepository extends JpaRepository<EmailRecipient, UUID> {
}
