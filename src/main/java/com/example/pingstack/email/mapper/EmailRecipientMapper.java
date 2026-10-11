package com.example.pingstack.email.mapper;


import com.example.pingstack.email.dto.EmailRecipientRequest;
import com.example.pingstack.email.dto.EmailRecipientResponse;
import com.example.pingstack.email.model.EmailRecipient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailRecipientMapper {

    public EmailRecipient toModel (EmailRecipientRequest emailRecipientRequest){
        EmailRecipient emailRecipient = new EmailRecipient();
        emailRecipient.setEmail(emailRecipientRequest.getEmail());
        emailRecipient.setName(emailRecipientRequest.getName());
        return emailRecipient;
    }

    public EmailRecipientResponse toResponse (EmailRecipient emailRecipient){
        return new EmailRecipientResponse(emailRecipient.getEmail(), emailRecipient.getName());
    }
}
