package com.example.pingstack.mail;


import brevo.ApiClient;
import brevo.Configuration;
import brevo.auth.ApiKeyAuth;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import com.example.pingstack.auth.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;




    @PostConstruct
    public void init() {
        // Initialize Brevo client authentication
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);
    }

    @Async
    public void sendWalletPendingReminder(User user){
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

        // 1. Define Sender
        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail("support@pingstack.com");
        sender.setName("Pingstack Team");

        // 2. Define Recipient
        SendSmtpEmailTo toRecipient = new SendSmtpEmailTo();
        toRecipient.setEmail(user.getEmail());
        toRecipient.setName(user.getFirstName());

        // 3. Compose Email Content
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setSender(sender);
        sendSmtpEmail.setTo(Collections.singletonList(toRecipient));
        sendSmtpEmail.setSubject("Action Required: Complete your Pingstack Wallet Setup");

        String htmlContent = "<h3>Hello " + user.getFirstName() + ",</h3>"
                + "<p>Your account registration was successful! However, we encountered a brief hiccup "
                + "while initializing your payment wallet.</p>"
                + "<p>Don't worry, your data is safe. Please log into your dashboard and click "
                + "<strong>'Retry Wallet Setup'</strong> to complete your account setup manually.</p>"
                + "<br/><p>Best regards,<br/>The Pingstack Team</p>";

        sendSmtpEmail.setHtmlContent(htmlContent);

        try {
            apiInstance.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            // Log if Brevo itself fails
            System.err.println("Exception when calling TransactionalEmailsApi over Brevo: " + e.getMessage());
        }
    }



}
