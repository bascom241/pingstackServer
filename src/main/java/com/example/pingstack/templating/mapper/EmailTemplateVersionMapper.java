package com.example.pingstack.templating.mapper;


import com.example.pingstack.templating.dtos.CreateEmailTemplateVersionRequest;
import com.example.pingstack.templating.dtos.EmailTemplateResponse;
import com.example.pingstack.templating.dtos.EmailTemplateVersionRequest;
import com.example.pingstack.templating.dtos.EmailTemplateVersionResponse;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailTemplateVersionMapper {


    public EmailTemplateVersion toEntity (EmailTemplateVersionRequest emailTemplateVersionRequest){
        EmailTemplateVersion emailTemplateVersion = new EmailTemplateVersion();
        emailTemplateVersion.setHtmlContent(emailTemplateVersionRequest.getHtmlContent());
        emailTemplateVersion.setSubject(emailTemplateVersion.getSubject());
        emailTemplateVersion.setDesignJson(emailTemplateVersion.getDesignJson());
        return emailTemplateVersion;
    }



    public EmailTemplateVersionResponse toResponse (EmailTemplateVersion emailTemplateVersion){
        return new EmailTemplateVersionResponse(
                emailTemplateVersion.getEmailTemplateVersionId(),
                emailTemplateVersion.getEmailTemplate().getEmailTemplateId(),
                emailTemplateVersion.getVersion(),
                emailTemplateVersion.getSubject(),
                emailTemplateVersion.getDesignJson(),
                emailTemplateVersion.getHtmlContent(),
                emailTemplateVersion.getCreatedBy().getFirstName()
        );
    }
}
