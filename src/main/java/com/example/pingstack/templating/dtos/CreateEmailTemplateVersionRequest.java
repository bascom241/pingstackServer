package com.example.pingstack.templating.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateEmailTemplateVersionRequest(
        @NotBlank
        String subject,

        @NotBlank
        String htmlContent
) {

}
