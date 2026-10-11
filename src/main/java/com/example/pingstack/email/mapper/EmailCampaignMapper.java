package com.example.pingstack.email.mapper;

import com.example.pingstack.email.dto.CampaignResponse;
import com.example.pingstack.email.dto.CreateEmailCampaignRequest;
import com.example.pingstack.email.dto.CreateEmailCampaignResponse;
import com.example.pingstack.email.dto.EmailRecipientResponse;
import com.example.pingstack.email.model.EmailCampaign;
import com.example.pingstack.email.model.EmailRecipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmailCampaignMapper {


    public EmailCampaign toModel (CreateEmailCampaignRequest createEmailCampaignRequest){
        EmailCampaign emailCampaign = new EmailCampaign();
        emailCampaign.setName(createEmailCampaignRequest.getName());
        return emailCampaign;
    }

    public CreateEmailCampaignResponse toResponse (EmailCampaign emailCampaign){
        return new CreateEmailCampaignResponse(
                emailCampaign.getId(),
                emailCampaign.getStatus()
        );
    }





    public CampaignResponse toResponseDto (EmailCampaign emailCampaign){
        return new CampaignResponse(
                emailCampaign.getId().toString(),
                emailCampaign.getUser().getUserId().toString(),
                emailCampaign.getTemplate().getEmailTemplateId().toString(),
                emailCampaign.getName(),
                emailCampaign.getSubject(),
                emailCampaign.getTemplateVersion().getEmailTemplateVersionId().toString(),
                emailCampaign.getStatus().toString(),
                emailCampaign.getTotalRecipients(),
                emailCampaign.getProcessedRecipients(),
                emailCampaign.getFailedRecipients(),
                emailCampaign.getCreatedAt().toString(),
                Optional.ofNullable(emailCampaign.getStartedAt())
                        .map(Object::toString)
                        .orElse(null)
        );
    }







}
