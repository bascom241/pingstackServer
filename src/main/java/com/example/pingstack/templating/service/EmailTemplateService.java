package com.example.pingstack.templating.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.templating.dtos.CreateTemplateRequestDto;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.dtos.EmailTemplateVersionRequest;
import com.example.pingstack.templating.dtos.EmailTemplateVersionResponse;
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

    public EmailTemplateVersionResponse editTemplateVersion (UUID templateId, String authHeader, EmailTemplateVersionRequest emailTemplateVersionRequest){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailTemplate emailTemplate = emailTemplateRepository.findById(templateId).orElseThrow(() -> new RuntimeException("Email Template not found "));
        boolean isOwned = emailTemplate.getCreatedBy().equals(user);
        if(!isOwned){
            throw new RuntimeException("Email Template does not belong to this user");
        }
        EmailTemplateVersion emailTemplateVersion = emailTemplateVersionRepository.findByEmailTemplate(emailTemplate).orElseThrow(() -> new RuntimeException("Template Version not found"));
        Integer currentVersion = emailTemplateVersion.getVersion() + 1;
        emailTemplateVersion.setVersion(currentVersion);
        if(emailTemplateVersionRequest.getDesignJson() != null  || emailTemplateVersionRequest.getDesignJson().isEmpty()) emailTemplateVersion.setDesignJson(emailTemplateVersionRequest.getDesignJson());
        if(emailTemplateVersionRequest.getHtmlContent() != null || emailTemplateVersionRequest.getHtmlContent().isEmpty()) emailTemplateVersion.setHtmlContent(emailTemplateVersionRequest.getHtmlContent());
        if(emailTemplateVersionRequest.getSubject() != null || emailTemplateVersionRequest.getSubject().isEmpty())   emailTemplateVersion.setSubject(emailTemplateVersionRequest.getSubject());
        EmailTemplateVersion savedEmailTemplateVersion = emailTemplateVersionMapper.toEntity(emailTemplateVersionRequest);
        EmailTemplateVersion emailTemplateVersion1 = emailTemplateVersionRepository.save(savedEmailTemplateVersion);
        return emailTemplateVersionMapper.toResponse(emailTemplateVersion1);
    }

    public EmailTemplateVersionResponse getSingleEmailTemplate (UUID templateId, String authHeader ){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailTemplate emailTemplate = emailTemplateRepository.findById(templateId).orElseThrow(() -> new RuntimeException("Email Template not found "));
        boolean isOwned = emailTemplate.getCreatedBy().equals(user);
        if(!isOwned){
            throw new RuntimeException("Email Template does not belong to this user");
        }

        EmailTemplateVersion emailTemplateVersion = emailTemplateVersionRepository.findByEmailTemplate(emailTemplate).orElseThrow(() -> new RuntimeException("Template Version not found"));
        return emailTemplateVersionMapper.toResponse(emailTemplateVersion);
    }



}
