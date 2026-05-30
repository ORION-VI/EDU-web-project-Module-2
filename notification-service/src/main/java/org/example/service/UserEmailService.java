package org.example.service;

import org.example.dto.UserEventDto;
import org.example.email.Email;
import org.example.email.EmailComposerInterface;
import org.example.email.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserEmailService implements EmailServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserEmailService.class);
    private EmailComposerInterface<UserEventDto> userEmailComposer;
    private EmailSender emailSender;

    public UserEmailService(EmailSender emailSender, EmailComposerInterface<UserEventDto> userEmailComposer) {
        this.emailSender = emailSender;
        this.userEmailComposer = userEmailComposer;
    }

    @Override
    public void sendEmailByEvent(UserEventDto userEventDto) {
        try {
            Email email = userEmailComposer.composeByEvent(userEventDto);
            emailSender.sendEmail(userEventDto.getUserEmail(), email.getSubject(), email.getBody());
        }
        catch(Exception e) {
            logger.error("ERROR: {}", String.valueOf(e));
        }
    }

    @Override
    public void sendEmail(String email, String subject, String body) {
        try {
            emailSender.sendEmail(email, subject, body);
        }
        catch(Exception e) {
            logger.error("ERROR: {}", String.valueOf(e));
        }
    }
}
