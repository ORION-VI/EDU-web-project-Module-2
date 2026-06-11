package org.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    public FallbackController() {
    }

    @GetMapping("/users")
    public ResponseEntity<String> userServiceFallbackResponse() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("User service is currently unavailable. " +
                "Please, try again later.");
    }

    @GetMapping("/notifications")
    public ResponseEntity<String> notificationServiceFallbackResponse() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Notification service is currently unavailable. " +
                "Please, try again later.");
    }
}
