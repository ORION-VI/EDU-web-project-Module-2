package org.example.notificator;

import jakarta.validation.Valid;
import org.example.dto.UserEventDto;
import org.example.service.EmailServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventReceiver {
    private static final Logger logger = LoggerFactory.getLogger(UserEventReceiver.class);
    private EmailServiceInterface userEmailService;

    public UserEventReceiver(EmailServiceInterface userEmailService) {
        this.userEmailService = userEmailService;
    }

    @KafkaListener(id = "notification-service-receiver",
            topics = "user-events",
            groupId = "notification-service-group")
    public void sendEmailAuto(UserEventDto userEventDto) {
        logger.info("MESSAGE (EVENT: {}, USERNAME: {}, EMAIL: {}) RECEIVED",
                userEventDto.getEvent(), userEventDto.getUserName(), userEventDto.getUserEmail());
        userEmailService.sendEmailByEvent(userEventDto);
    }
}
