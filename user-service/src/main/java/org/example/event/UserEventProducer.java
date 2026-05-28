package org.example.event;

import org.example.dto.UserEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {
    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;
    private static final String TOPIC_NAME = "user-events";

    public UserEventProducer(KafkaTemplate<String, UserEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String event, String name, String email) {
        UserEventDto userEventDto = UserEventDto.buildUserEventDto(event, name, email);
        kafkaTemplate.send(TOPIC_NAME, email, userEventDto);
    }
}
