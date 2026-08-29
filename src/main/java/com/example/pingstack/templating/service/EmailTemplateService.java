package com.example.pingstack.templating.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.templating.dtos.CreateTemplateRequestDto;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.mapper.EmailTemplateMapper;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import com.example.pingstack.templating.repository.EmailTemplateRepository;
import com.example.pingstack.templating.repository.EmailTemplateVersionRepository;
import com.example.pingstack.templating.util.SlugUtil;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
        emailTemplateVersion.setCreatedBy(user);
        emailTemplateVersion.setCreatedAt(LocalDateTime.now());
        emailTemplate.getVersions().add(emailTemplateVersion);
        emailTemplateRepository.save(emailTemplate);
        EmailTemplate savedTemplate = emailTemplateRepository.save(emailTemplate);
        return emailTemplateMapper.toResponse(savedTemplate);

    }
}
