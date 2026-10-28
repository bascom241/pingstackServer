package com.example.pingstack.email.service.brevo;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.dto.CampaignResponse;
import com.example.pingstack.email.dto.CreateEmailCampaignRequest;
import com.example.pingstack.email.dto.CreateEmailCampaignResponse;
import com.example.pingstack.email.dto.EmailRecipientRequest;
import com.example.pingstack.email.enums.CampaignStatus;
import com.example.pingstack.email.enums.RecipientStatus;
import com.example.pingstack.email.mapper.EmailCampaignMapper;
import com.example.pingstack.email.mapper.EmailRecipientMapper;
import com.example.pingstack.email.model.EmailCampaign;
import com.example.pingstack.email.model.EmailRecipient;
import com.example.pingstack.email.repository.EmailCampaignRepository;
import com.example.pingstack.email.repository.EmailRecipientRepository;
import com.example.pingstack.email.service.EmailCampaignService;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import com.example.pingstack.exceptions.unauthorized.AccessDeniedException;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import com.example.pingstack.templating.repository.EmailTemplateRepository;
import com.example.pingstack.templating.repository.EmailTemplateVersionRepository;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BrevoCampaignService implements EmailCampaignService {

    @Autowired
    private EmailCampaignRepository emailCampaignRepository;

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private EmailCampaignMapper emailCampaignMapper;

    @Autowired
    private EmailRecipientMapper emailRecipientMapper;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailTemplateVersionRepository emailTemplateVersionRepository;

    @Autowired
    private EmailRecipientRepository emailRecipientRepository;

    @Autowired
    private AsyncCampaignProcessingService asyncCampaignProcessingService;


    @Override
    @Transactional
    public CreateEmailCampaignResponse create(
            String authHeader,
            CreateEmailCampaignRequest createEmailCampaignRequest
    ) {
        // 1. Validate the request
        if (createEmailCampaignRequest.getRecipientRequests() == null
                || createEmailCampaignRequest.getRecipientRequests().isEmpty()) {
            throw new IllegalArgumentException(
                    "A campaign must have at least one recipient"
            );
        }

        // 2. Get the authenticated user
        User user = extractUserInfoFromAuthHeader
                .extractUserInfo(authHeader);

        // 3. Find the email template
        EmailTemplate emailTemplate = emailTemplateRepository
                .findById(UUID.fromString(
                        createEmailCampaignRequest.getTemplateId()
                ))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email Template not found")
                );

        // 4. Verify template ownership
        if (!emailTemplate.getCreatedBy().equals(user)) {
            throw new AccessDeniedException(
                    "You are not authorized to use this email template"
            );
        }

        // 5. Get the current template version
        EmailTemplateVersion currentTemplateVersion =
                emailTemplateVersionRepository
                        .findByEmailTemplateAndVersion(
                                emailTemplate,
                                emailTemplate.getCurrentVersion()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Current Template version not found"
                                )
                        );

        // 6. Create the campaign
        EmailCampaign newEmailCampaign =
                emailCampaignMapper.toModel(createEmailCampaignRequest);

        newEmailCampaign.setTemplate(emailTemplate);
        newEmailCampaign.setTemplateVersion(currentTemplateVersion);
        newEmailCampaign.setUser(user);

        // 7. Set the campaign subject
        String subject = createEmailCampaignRequest.getSubject() != null
                && !createEmailCampaignRequest.getSubject().isBlank()
                ? createEmailCampaignRequest.getSubject()
                : currentTemplateVersion.getSubject();

        newEmailCampaign.setSubject(subject);

        // 8. Initialize campaign status and counters
        newEmailCampaign.setStatus(CampaignStatus.QUEUED);
        newEmailCampaign.setTotalRecipients(
                createEmailCampaignRequest.getRecipientRequests().size()
        );
        newEmailCampaign.setProcessedRecipients(0);
        newEmailCampaign.setSuccessfulRecipients(0);
        newEmailCampaign.setFailedRecipients(0);

        // 9. Save the campaign
        EmailCampaign savedCampaign =
                emailCampaignRepository.save(newEmailCampaign);

        // 10. Map recipient requests to recipient entities
        List<EmailRecipient> recipients =
                createEmailCampaignRequest.getRecipientRequests()
                        .stream()
                        .map(recipientRequest -> {
                            EmailRecipient recipient =
                                    emailRecipientMapper.toModel(recipientRequest);

                            recipient.setStatus(RecipientStatus.PENDING);
                            recipient.setCampaign(savedCampaign);

                            return recipient;
                        })
                        .toList();

        // 11. Save all recipients
        emailRecipientRepository.saveAll(recipients);

        // 12. Return the campaign response
        return emailCampaignMapper.toResponse(savedCampaign);
    }


    public Page<CampaignResponse> getCampaigns (String authHeader,  int page, int size){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<EmailCampaign> emailCampaigns = emailCampaignRepository.findById(user, pageable);
        return emailCampaigns.map(emailCampaignMapper::toResponseDto);
    }

    // CAMPAIGN QUEUE


    @Override
    public void triggerProcessing(String campaignId, String authHeader) {

        EmailCampaign emailCampaign = emailCampaignRepository
                .findById(UUID.fromString(campaignId))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found")
                );

        User user = extractUserInfoFromAuthHeader
                .extractUserInfo(authHeader);

        if (!emailCampaign.getUser().equals(user)) {
            throw new AccessDeniedException(
                    "You are not authorized to process this campaign"
            );
        }

        if (emailCampaign.getStatus() != CampaignStatus.QUEUED) {
            throw new IllegalStateException(
                    "Only queued campaigns can be processed"
            );
        }

        asyncCampaignProcessingService.processCampaign(campaignId);
    }


    // Create Add Recipients end point



    // Add Recipient Endpoint
    // Utils to add
    public void addRecipientToCampaign(String authHeader,  String campaignId, List<EmailRecipientRequest> emailRecipientRequests){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailCampaign emailCampaign = emailCampaignRepository.findById(UUID.fromString(campaignId)).orElseThrow(() -> new ResourceNotFoundException("campaign not found"));
        if(!emailCampaign.getUser().equals(user)){
            throw new AccessDeniedException("You are not authorized to this action");
        }
        List<EmailRecipient> recipients = emailRecipientRequests.stream().map(emailRecipientRequest -> {
            EmailRecipient savedRecipient = emailRecipientMapper.toModel(emailRecipientRequest);
            savedRecipient.setStatus(RecipientStatus.PENDING);
            savedRecipient.setCampaign(emailCampaign);
            return savedRecipient;
        }).toList();
        emailRecipientRepository.saveAll(recipients);
    }



}
