package com.example.pingstack.templating.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.templating.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, UUID> {
    Optional<EmailTemplate> findBySlug(String slug);
    List<EmailTemplate> findByUser(User user);
}