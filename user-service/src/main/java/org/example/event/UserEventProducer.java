package org.example.event;

import org.example.dto.UserEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {
    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;

    @Value("${kafka.topic.user-events:user-events}")
    private String topicName;

    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    public UserEventProducer(KafkaTemplate<String, UserEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String event, String name, String email) {
        UserEventDto userEventDto = UserEventDto.buildUserEventDto(event, name, email);
        try {
            kafkaTemplate.send(topicName, email, userEventDto);
        } catch (Exception e) {
            logger.error("FAILED TO SEND MESSAGE (EVENT: {}, USERNAME: {}, USER EMAIL: {}) TO KAFKA: {}",
                    event, name, email, e.getMessage());
        }
        logger.info("MESSAGE (EVENT: {}, USERNAME: {}, USER EMAIL: {}) WAS SENT", event, name, email);
    }
}
