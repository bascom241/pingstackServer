package com.example.pingstack.email.service.brevo;


import com.example.pingstack.email.dto.brevo.BrevoWebhookEvent;
import com.example.pingstack.email.enums.RecipientStatus;
import com.example.pingstack.email.model.EmailRecipient;
import com.example.pingstack.email.repository.EmailRecipientRepository;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class BrevoWebhookService {


    @Autowired
    private EmailRecipientRepository emailRecipientRepository;


    @Transactional
    public void processEvent(BrevoWebhookEvent event){


        EmailRecipient emailRecipient = emailRecipientRepository.findByProviderMessageId(event.getMessageId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found for Brevo message ID: "
                        + event.getMessageId()));

        String eventName = event.getEvent()
                .trim()
                .toLowerCase(Locale.ROOT);

        switch (eventName) {
            case "delivered" -> {
                if(emailRecipient.getStatus() != RecipientStatus.BOUNCED){
                    emailRecipient.setStatus(RecipientStatus.DELIVERED);
                    emailRecipient.setDeliveredAt(LocalDateTime.now());
                }
            }

            case "hard_bounce", "hardbounce" -> {
                emailRecipient.setStatus(RecipientStatus.BOUNCED);
                emailRecipient.setLastError(event.getReason());
            }

            case "soft_bounce", "softbounce" -> System.out.println(
                    "Soft bounce for " + emailRecipient.getEmail()
                            + ": " + event.getReason()
            );

            case "opened", "unique_opened", "proxy_open",
                 "unique_proxy_open", "click" -> System.out.println(
                         "Engagement event " + eventName
                                 + " for " + emailRecipient.getEmail()
                 );

            default -> System.out.println(
                    "Ignoring unsupported Brevo event: " + eventName
            );

        }

        emailRecipientRepository.save(emailRecipient);

        System.out.println(
                "Updated recipient " + emailRecipient.getEmail()
                        + " with event " + eventName
                        + "; status = " + emailRecipient.getStatus()
        );
    }
}
