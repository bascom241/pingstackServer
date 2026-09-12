package com.example.pingstack.templating.repository;

import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailTemplateVersionRepository extends JpaRepository<EmailTemplateVersion, UUID> {
   Optional<EmailTemplateVersion> findByEmailTemplate(EmailTemplate emailTemplate);
}
