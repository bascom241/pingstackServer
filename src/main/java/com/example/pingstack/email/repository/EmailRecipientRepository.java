package com.example.pingstack.email.repository;

import com.example.pingstack.email.enums.RecipientStatus;
import com.example.pingstack.email.model.EmailCampaign;
import com.example.pingstack.email.model.EmailRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRecipientRepository extends JpaRepository<EmailRecipient, UUID> {
    Page<EmailRecipient> findByCampaignAndStatus(EmailCampaign emailCampaign, RecipientStatus recipientStatus, Pageable pageable);
    Optional<EmailRecipient> findByProviderMessageId(String providerMessageId);


}
