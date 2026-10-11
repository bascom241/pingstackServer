package com.example.pingstack.system;

import com.example.pingstack.templating.enums.EmailTemplateCategory;
import com.example.pingstack.templating.enums.EmailTemplateStatus;
import com.example.pingstack.templating.enums.TemplateType;
import com.example.pingstack.templating.model.EmailTemplate;
import com.example.pingstack.templating.model.EmailTemplateVersion;
import com.example.pingstack.templating.repository.EmailTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemTemplateInitializer implements CommandLineRunner {

    private final EmailTemplateRepository emailTemplateRepository;

    public SystemTemplateInitializer(
            EmailTemplateRepository emailTemplateRepository
    ) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public void run(String... args) {

        createForgotPasswordTemplate();

    }

    private void createForgotPasswordTemplate() {

        // Check whether the system template already exists
        if (emailTemplateRepository
                .findBySlugAndTemplateType(
                        "forgot-password",
                        TemplateType.SYSTEM
                )
                .isPresent()) {

            return;
        }

        // =========================
        // Create Email Template
        // =========================

        EmailTemplate emailTemplate = new EmailTemplate();

        emailTemplate.setEmailName("Forgot Password");

        emailTemplate.setEmailTemplateDescription(
                "System template used to send password reset emails."
        );

        emailTemplate.setEmailTemplateCategory(
                EmailTemplateCategory.TRANSACTIONAL
        );

        emailTemplate.setSlug("forgot-password");

        emailTemplate.setEmailTemplateStatus(
                EmailTemplateStatus.PUBLISHED
        );

        emailTemplate.setTemplateType(
                TemplateType.SYSTEM
        );

        // System templates don't belong to a user
        emailTemplate.setCreatedBy(null);

        emailTemplate.setCurrentVersion(1);

        emailTemplate.setUpdatedAt(
                LocalDateTime.now()
        );

        // =========================
        // Create Template Version
        // =========================

        EmailTemplateVersion version =
                new EmailTemplateVersion();

        version.setVersion(1);

        version.setSubject(
                "Reset your Pingstack password"
        );

        version.setHtmlContent("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Reset your password</title>
                </head>

                <body>

                    <h2>Reset your password</h2>

                    <p>
                        Hello {{params.name}},
                    </p>

                    <p>
                        We received a request to reset your
                        Pingstack password.
                    </p>

                    <p>
                        Click the button below to create a new password.
                    </p>

                    <p>
                        <a href="{{params.resetUrl}}">
                            Reset Password
                        </a>
                    </p>

                    <p>
                        This link will expire in 15 minutes.
                    </p>

                    <p>
                        If you did not request a password reset,
                        you can safely ignore this email.
                    </p>

                </body>
                </html>
                """);

        /*
         * You can replace this with the actual Unlayer
         * design JSON later.
         */
        version.setDesignJson("{}");

        // System template version isn't created by a user
        version.setCreatedBy(null);

        version.setCreatedAt(
                LocalDateTime.now()
        );

        // =========================
        // Connect Version → Template
        // =========================

        version.setEmailTemplate(emailTemplate);

        emailTemplate.getVersions().add(version);

        // =========================
        // Save
        // =========================

        emailTemplateRepository.save(emailTemplate);
    }
}