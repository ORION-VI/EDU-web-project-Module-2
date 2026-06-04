package org.example.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Notifications", description = "API endpoints to send messages and notifications")
@RestController
@RequestMapping("/api/notifications")
public class UserNotificationController {
    private final EmailServiceInterface userEmailService;
    private final static Logger logger = LoggerFactory.getLogger(UserNotificationController.class);

    public UserNotificationController(EmailServiceInterface userEmailService) {
        this.userEmailService = userEmailService;
    }

    @Operation(summary = "Send an email", description = "Sends and email using provided email address, subject and message body")
    @ApiResponse(responseCode = "200", description = "Email was sent successfully",
    content = @Content(schema = @Schema(implementation = String.class, example = "Email sending was successful!")))
    @ApiResponse(responseCode = "500", description = "Failed to send an email because something happened")
    @ApiResponse(responseCode = "400", description = "Failed to send an email because provided parameters are invalid")
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
