package com.example.pingstack.templating.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.templating.dtos.CreateTemplateRequestDto;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.dtos.EmailTemplateVersionRequest;
import com.example.pingstack.templating.dtos.EmailTemplateVersionResponse;
import com.example.pingstack.templating.enums.TemplateType;
import com.example.pingstack.templating.mapper.EmailTemplateMapper;
import com.example.pingstack.templating.mapper.EmailTemplateVersionMapper;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import com.example.pingstack.templating.repository.EmailTemplateRepository;
import com.example.pingstack.templating.repository.EmailTemplateVersionRepository;
import com.example.pingstack.templating.util.SlugUtil;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailTemplateService {

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private EmailTemplateMapper emailTemplateMapper;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailTemplateVersionRepository emailTemplateVersionRepository;

    @Autowired
    private EmailTemplateVersionMapper  emailTemplateVersionMapper;




    public EmailTemplateResponse createTemplate (String authHeader, CreateTemplateRequestDto createTemplateRequestDto){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        String createdSlug = SlugUtil.toSlug(createTemplateRequestDto.getSlug());
        Optional <EmailTemplate> isEmailTemplateExist = emailTemplateRepository.findBySlug(createdSlug);
        if(isEmailTemplateExist.isPresent()){
           throw new RuntimeException("Slug exist for this template");
        }
        EmailTemplate emailTemplate = emailTemplateMapper.toEntity(createTemplateRequestDto);
        emailTemplate.setCreatedBy(user);
        emailTemplate.setTemplateType(TemplateType.USER);
        emailTemplate.setSlug(createdSlug);
        emailTemplate.setCurrentVersion(1);
        emailTemplate.setUpdatedAt(LocalDateTime.now());
        EmailTemplateVersion emailTemplateVersion = new EmailTemplateVersion();
        emailTemplateVersion.setVersion(1);
        emailTemplateVersion.setSubject(createTemplateRequestDto.getVersion().subject());
        emailTemplateVersion.setHtmlContent(createTemplateRequestDto.getVersion().htmlContent());
        emailTemplateVersion.setDesignJson(createTemplateRequestDto.getVersion().designJson());
        emailTemplateVersion.setCreatedBy(user);
        emailTemplateVersion.setCreatedAt(LocalDateTime.now());
        emailTemplateVersion.setEmailTemplate(emailTemplate);
        emailTemplate.getVersions().add(emailTemplateVersion);

        EmailTemplate savedTemplate = emailTemplateRepository.save(emailTemplate);
        return emailTemplateMapper.toResponse(savedTemplate);
    }


    public List<EmailTemplateResponse> getAllEmailTemplateForUser (String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        List<EmailTemplate> emailTemplates = emailTemplateRepository.findByCreatedBy(user);
        return emailTemplates.stream().map(emailTemplate -> emailTemplateMapper.toResponse(emailTemplate)).toList();
    }


    public EmailTemplateVersionResponse editTemplateVersion(
            UUID templateId,
            String authHeader,
            EmailTemplateVersionRequest request
    ) {
        User user = extractUserInfoFromAuthHeader
                .extractUserInfo(authHeader);
        EmailTemplate emailTemplate = emailTemplateRepository
                .findById(templateId)
                .orElseThrow(() ->
                        new RuntimeException("Email Template not found")
                );
        if (!emailTemplate.getCreatedBy().equals(user)) {
            throw new RuntimeException(
                    "Email Template does not belong to this user"
            );
        }
        Integer nextVersion =
                emailTemplate.getCurrentVersion() + 1;

        EmailTemplateVersion newVersion =
                new EmailTemplateVersion();
        newVersion.setVersion(nextVersion);
        newVersion.setSubject(request.getSubject());
        newVersion.setHtmlContent(request.getHtmlContent());
        newVersion.setDesignJson(request.getDesignJson());
        newVersion.setCreatedBy(user);
        newVersion.setCreatedAt(LocalDateTime.now());
        newVersion.setEmailTemplate(emailTemplate);
        EmailTemplateVersion savedVersion =
                emailTemplateVersionRepository.save(newVersion);
        emailTemplate.setCurrentVersion(nextVersion);
        emailTemplate.setUpdatedAt(LocalDateTime.now());
        emailTemplateRepository.save(emailTemplate);
        return emailTemplateVersionMapper.toResponse(savedVersion);
    }


    public EmailTemplateVersionResponse getSingleEmailTemplate(
            UUID templateId,
            String authHeader
    ) {
        User user =
                extractUserInfoFromAuthHeader.extractUserInfo(authHeader);

        EmailTemplate emailTemplate =
                emailTemplateRepository.findById(templateId)
                        .orElseThrow(() ->
                                new RuntimeException("Email Template not found")
                        );

        boolean isOwned =
                emailTemplate.getCreatedBy().equals(user);

        if (!isOwned) {
            throw new RuntimeException(
                    "Email Template does not belong to this user"
            );
        }

        EmailTemplateVersion emailTemplateVersion =
                emailTemplateVersionRepository
                        .findByEmailTemplateAndVersion(
                                emailTemplate,
                                emailTemplate.getCurrentVersion()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Current template version not found"
                                )
                        );

        return emailTemplateVersionMapper.toResponse(
                emailTemplateVersion
        );
    }






}
