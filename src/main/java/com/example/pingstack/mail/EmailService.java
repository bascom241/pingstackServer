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
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Autowired
    private HtmlContent htmlContent;


    @PostConstruct
    public void init() {
        // Initialize Brevo client authentication
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);
    }


    private static @NonNull SendSmtpEmail getSendSmtpEmail(User user, SenderRequestData senderRequestData) {
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setSender(senderRequestData.getSendSmtpEmailSender());
        sendSmtpEmail.setTo(Collections.singletonList(senderRequestData.getSendSmtpEmailTo()));
        sendSmtpEmail.setSubject(senderRequestData.getSubject());
        sendSmtpEmail.setHtmlContent(senderRequestData.getHtmlContent());
        return sendSmtpEmail;
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

        //2b. Build the subject
        String emailSubject = "Action Required: Complete your Pingstack Wallet Setup";

        // 3A build the email content
        SenderRequestData senderRequestDataToSend = new SenderRequestData(
                sender,
                toRecipient,
                emailSubject,
                HtmlContent.walletReminderHtmlContent(user)

        );


        // 3. Compose Email Content
        SendSmtpEmail sendSmtpEmail = getSendSmtpEmail(user, senderRequestDataToSend);

        try {
            apiInstance.sendTransacEmail(sendSmtpEmail);

        } catch (Exception e) {
            // Log if Brevo itself fails
            System.err.println("Exception when calling TransactionalEmailsApi over Brevo: " + e.getMessage());
        }
    }



    @Async
    public void sendForgotPassword (User user, String resetUrl, String token){
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        
        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail("abdulbasitabdulwahab21@gmail.com");
        sender.setName("pingstack");
        
        SendSmtpEmailTo toRecipient = new SendSmtpEmailTo();
        toRecipient.setEmail(user.getEmail());
        toRecipient.setName(user.getFirstName());

        String emailSubject = "Forgot your password? We’ve got you covered.";

        SenderRequestData senderRequestDataToSend = new SenderRequestData(
                sender,
                toRecipient,
                emailSubject,
                htmlContent.forgotPassword(user,resetUrl, token )
        );
        SendSmtpEmail sendSmtpEmail = getSendSmtpEmail(user, senderRequestDataToSend);
        try {
            apiInstance.sendTransacEmail(sendSmtpEmail);
        }  catch (Exception e) {
            // Log if Brevo itself fails
            System.err.println("Exception when calling TransactionalEmailsApi over Brevo: " + e.getMessage());
        }
        
    }










}
