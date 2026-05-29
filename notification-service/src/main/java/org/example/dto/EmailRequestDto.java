package org.example.dto;

import jakarta.validation.constraints.*;

public class EmailRequestDto {
    @Email
    @NotNull
    @NotEmpty
    @NotBlank
    private final String email;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 70)
    private final String subject;

    @NotNull
    @NotEmpty
    @NotBlank
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
