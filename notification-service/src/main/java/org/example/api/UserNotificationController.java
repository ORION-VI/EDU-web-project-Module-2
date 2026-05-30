package org.example.api;

import jakarta.validation.Valid;
import org.example.dto.EmailRequestDto;
import org.example.service.EmailServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class UserNotificationController {
    private EmailServiceInterface userEmailService;
    private final static Logger logger = LoggerFactory.getLogger(UserNotificationController.class);

    public UserNotificationController(EmailServiceInterface userEmailService) {
        this.userEmailService = userEmailService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmailManually(@Valid @RequestBody EmailRequestDto emailRequest) {
        logger.info("EMAIL SEND REQUEST RECEIVED WITH EMAIL: {}, SUBJECT: {}, BODY: {}",
                emailRequest.getEmail(), emailRequest.getSubject(), emailRequest.getBody());
        try {
            userEmailService.sendEmail(emailRequest.getEmail(), emailRequest.getSubject(), emailRequest.getBody());
            return ResponseEntity.ok().body("Email sending was successful!");
        } catch (Exception e) {
            logger.error("ERROR: {}", String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Oops! Something happened. We'll fix it soon!");
        }
    }
}
