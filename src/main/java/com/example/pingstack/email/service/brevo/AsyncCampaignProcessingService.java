package com.example.pingstack.email.service.brevo;


import com.example.pingstack.email.enums.CampaignStatus;
import com.example.pingstack.email.enums.RecipientStatus;
import com.example.pingstack.email.model.EmailCampaign;
import com.example.pingstack.email.model.EmailRecipient;
import com.example.pingstack.email.repository.EmailCampaignRepository;
import com.example.pingstack.email.repository.EmailRecipientRepository;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AsyncCampaignProcessingService {


    @Autowired
    private EmailCampaignRepository emailCampaignRepository;

    @Autowired
    private EmailRecipientRepository emailRecipientRepository;

    @Autowired
    private BrevoSendingEmailService brevoSendingEmailService;

    @Transactional
    @Async
    public void processCampaign (String campaignId){
        EmailCampaign campaign = emailCampaignRepository
                .findById(UUID.fromString(campaignId))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found")
                );
        if (campaign.getStatus() != CampaignStatus.QUEUED) {
            System.out.println(
                    "Campaign is not queued: " + campaign.getId()
            );

            return;
        }


        campaign.setStatus(CampaignStatus.PROCESSING);
        emailCampaignRepository.save(campaign);

        System.out.println(
                "Background worker started processing campaign: "
                        + campaign.getId()
        );

        Pageable pageable = PageRequest.of(0, 100);

        while(true) {


            Page<EmailRecipient> recipientBatch = emailRecipientRepository.findByCampaignAndStatus(campaign, RecipientStatus.PENDING, pageable);

            System.out.println("Recipient in first Batch:" + recipientBatch.getTotalElements());

            if (recipientBatch.isEmpty()) {
                break;
            }

            var templateVersion = campaign.getTemplateVersion();

            for (EmailRecipient emailRecipient : recipientBatch.getContent()) {
                System.out.println("Processing Recipient" + emailRecipient.getEmail());
                try {
                    String messageId = brevoSendingEmailService.sendEmail(
                            emailRecipient.getName(),
                            emailRecipient.getEmail(),
                            templateVersion.getSubject(),
                            templateVersion.getHtmlContent()
                    );

                    emailRecipient.setProviderMessageId(messageId);
                    emailRecipient.setStatus(RecipientStatus.SENT);
                    emailRecipient.setSentAt(LocalDateTime.now());

                    int attempts = emailRecipient.getAttempts() == null ? 0 : emailRecipient.getAttempts();

                    emailRecipient.setAttempts(attempts + 1);

                    emailRecipientRepository.save(emailRecipient);
                    campaign.setStatus(CampaignStatus.PROCESSING);
                    System.out.println("Email accepted by brevo for " + emailRecipient.getEmail() + ". messageId: " + messageId);


                } catch (Exception e) {
                    int attempts = emailRecipient.getAttempts() == null ? 0 : emailRecipient.getAttempts();

                    emailRecipient.setAttempts(attempts + 1);
                    emailRecipient.setLastError(e.getMessage());

                    emailRecipientRepository.save(emailRecipient);

                    System.err.println(
                            "Failed to send email to "
                                    + emailRecipient.getEmail()
                                    + ": " + e.getMessage()
                    );

                    throw new RuntimeException("Error processing campaign batch item for: " + emailRecipient.getEmail(), e);
                }
            }


            System.out.println(
                    "Finished processing first batch for campaign: "
                            + campaign.getId()
            );

        }


        int successful = campaign.getSuccessfulRecipients();
        int failed = campaign.getFailedRecipients();

        if (failed == 0) {
            campaign.setStatus(CampaignStatus.COMPLETED);
        } else if (successful == 0) {
            campaign.setStatus(CampaignStatus.FAILED);
        } else {
            campaign.setStatus(CampaignStatus.PARTIALLY_FAILED);
        }

        emailCampaignRepository.save(campaign);

        System.out.println(
                "Campaign processing finished. Campaign: "
                        + campaign.getId()
                        + ", processed: " + campaign.getProcessedRecipients()
                        + ", successful: " + successful
                        + ", failed: " + failed
                        + ", status: " + campaign.getStatus()
        );
    }
}