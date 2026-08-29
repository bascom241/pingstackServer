package com.example.pingstack.templating.mapper;

import com.example.pingstack.templating.dtos.CreateTemplateRequestDto;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.model.EmailTemplate;

public class EmailTemplateMapper {


    public EmailTemplate toEntity (CreateTemplateRequestDto createTemplateRequestDto){
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setEmailName(createTemplateRequestDto.getName());
        emailTemplate.setEmailTemplateDescription(createTemplateRequestDto.getDescription());
        emailTemplate.setEmailTemplateCategory(createTemplateRequestDto.getCategory());
        emailTemplate.setEmailTemplateStatus(createTemplateRequestDto.getStatus());
        return emailTemplate;
    }


    public EmailTemplateResponse toResponse (EmailTemplate emailTemplate){
        return new EmailTemplateResponse(
                emailTemplate.getEmailName(),
                emailTemplate.getEmailTemplateDescription(),
                emailTemplate.getEmailTemplateCategory(),
                emailTemplate.getSlug(),
                emailTemplate.getEmailTemplateStatus(),
                emailTemplate.getCurrentVersion(),
                emailTemplate.getCreatedBy().getFirstName(),
                emailTemplate.getCreatedAt().toString(),
                emailTemplate.getUpdatedAt().toString()
        );
    }
}
