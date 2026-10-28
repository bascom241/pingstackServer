package com.example.pingstack.email.controller.brevo;


import com.example.pingstack.email.dto.brevo.BrevoWebhookEvent;
import com.example.pingstack.email.service.brevo.BrevoWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/webhooks/brevo")
public class BrevoWebhookController {


    @Value("${pingstack.webhook.brevo-token}")
    private String webhookToken;

    @Autowired
    private BrevoWebhookService brevoWebhookService;

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestHeader(
            value = "Authorization",
            required = false
    ) String authorization, @RequestBody BrevoWebhookEvent event){

        if (authorization == null ||
                !java.security.MessageDigest.isEqual(
                        ("Bearer " + webhookToken)
                                .getBytes(java.nio.charset.StandardCharsets.UTF_8),
                        authorization.getBytes(
                                java.nio.charset.StandardCharsets.UTF_8
                        )
                )) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }



        if (event.getEvent() == null || event.getMessageId() == null) {
            return ResponseEntity.badRequest()
                    .body("Missing required webhook fields");
        }

        brevoWebhookService.processEvent(event);


        System.out.println("Brevo webhook received");
        System.out.println("Event: " + event.getEvent());
        System.out.println("Message ID: " + event.getMessageId());



        return ResponseEntity.status(HttpStatus.OK).body("webhook received");
    }
}
