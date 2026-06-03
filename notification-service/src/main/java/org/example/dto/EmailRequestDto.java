package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class EmailRequestDto {
    @Email
    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "Email address of a message recipient", example = "smthsmth@smoogle.com")
    private final String email;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 70)
    @Schema(description = "Subject of an email", example = "Another message to feed your spam filter",
            minimum = "1 character", maximum = "70 characters")
    private final String subject;

    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "Main text of an email", example = "Lorem ipsum yada-yada and stuff")
    private final String body;

    public EmailRequestDto(String email, String subject, String body) {
        this.email = email;
        this.subject = subject;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }
}
