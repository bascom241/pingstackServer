package com.example.pingstack.email.service.brevo;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.dto.CampaignResponse;
import com.example.pingstack.email.dto.CreateEmailCampaignRequest;
import com.example.pingstack.email.dto.CreateEmailCampaignResponse;
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

    @Override
    @Transactional
    public CreateEmailCampaignResponse create(String authHeader, CreateEmailCampaignRequest createEmailCampaignRequest) {


        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailTemplate emailTemplate = emailTemplateRepository.findById(UUID.fromString(createEmailCampaignRequest.getTemplateId())).orElseThrow(() -> new ResourceNotFoundException("Email Template not found"));
        if(!emailTemplate.getCreatedBy().equals(user)) throw new AccessDeniedException("You are not authorized");
        EmailTemplateVersion currentCurrentVersion = emailTemplateVersionRepository.findByEmailTemplateAndVersion(emailTemplate, emailTemplate.getCurrentVersion()).orElseThrow(() -> new ResourceNotFoundException("Current Template version not found"));
        EmailCampaign newEmailCampaign = emailCampaignMapper.toModel(createEmailCampaignRequest);
        newEmailCampaign.setTemplate(emailTemplate);
        newEmailCampaign.setTemplateVersion(currentCurrentVersion);
        newEmailCampaign.setStatus(CampaignStatus.QUEUED);
        newEmailCampaign.setTotalRecipients(createEmailCampaignRequest.getRecipientRequests().size());
        newEmailCampaign.setProcessedRecipients(0);
        newEmailCampaign.setSuccessfulRecipients(0);
        newEmailCampaign.setFailedRecipients(0) ;
        newEmailCampaign.setUser(user);

        if( createEmailCampaignRequest.getSubject() != null) {
            newEmailCampaign.setSubject(currentCurrentVersion.getSubject());
        }
        newEmailCampaign.setSubject(currentCurrentVersion.getSubject());

        EmailCampaign savedCampaign = emailCampaignRepository.save(newEmailCampaign);

        // Up to this point we saved the campaign

        List<EmailRecipient> recipients = createEmailCampaignRequest.getRecipientRequests().
                stream().map(recipient -> {
                    EmailRecipient savedRecipient  = emailRecipientMapper.toModel(recipient);
                    savedRecipient.setStatus(RecipientStatus.PENDING);
                    savedRecipient.setCampaign(savedCampaign);
                    return savedRecipient;
                }).toList();

        emailRecipientRepository.saveAll(recipients);
        return emailCampaignMapper.toResponse(savedCampaign);

    }

    public Page<CampaignResponse> getCampaigns (String authHeader,  int page, int size){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<EmailCampaign> emailCampaigns = emailCampaignRepository.findById(user, pageable);
        return emailCampaigns.map(emailCampaignMapper::toResponseDto);
    }


}
