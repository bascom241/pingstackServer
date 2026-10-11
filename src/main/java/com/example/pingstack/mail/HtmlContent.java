package com.example.pingstack.mail;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import com.example.pingstack.templating.enums.TemplateType;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import com.example.pingstack.templating.repository.EmailTemplateRepository;
import com.example.pingstack.templating.repository.EmailTemplateVersionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component
public class HtmlContent {


    private final   EmailTemplateRepository emailTemplateRepository;
    private final  EmailTemplateVersionRepository emailTemplateVersionRepository;

    public HtmlContent(EmailTemplateRepository emailTemplateRepository, EmailTemplateVersionRepository emailTemplateVersionRepository) {
        this.emailTemplateRepository = emailTemplateRepository;


        this.emailTemplateVersionRepository = emailTemplateVersionRepository;
    }

    public static String  walletReminderHtmlContent (User data){
        return "<h3>Hello " + data.getFirstName() + ",</h3>"
                + "<p>Your account registration was successful! However, we encountered a brief hiccup "
                + "while initializing your payment wallet.</p>"
                + "<p>Don't worry, your data is safe. Please log into your dashboard and click "
                + "<strong>'Retry Wallet Setup'</strong> to complete your account setup manually.</p>"
                + "<br/><p>Best regards,<br/>The Pingstack Team</p>";

    }

    public  @NotNull String getEmailVersionHtml (){
        EmailTemplate emailTemplate = emailTemplateRepository.findBySlugAndTemplateType("forgot-password", TemplateType.SYSTEM).orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        EmailTemplateVersion emailTemplateVersion = emailTemplateVersionRepository.findByEmailTemplateAndVersion(emailTemplate, emailTemplate.getCurrentVersion()).orElseThrow(() -> new ResourceNotFoundException("Version not found"));

        return emailTemplateVersion.getHtmlContent();
    }

    public  String forgotPassword(User user, String resetUrl, String token){

        String rawHtml =  getEmailVersionHtml();



        System.out.println(rawHtml);

        String convertedHtml =  rawHtml
                .replace("{{params.name}}", user.getFirstName())
                .replace("{{params.resetUrl}}", resetUrl)
                .replace("{{params.otpCode}}", token);

        System.out.println(convertedHtml);
        return convertedHtml;
    }





}
