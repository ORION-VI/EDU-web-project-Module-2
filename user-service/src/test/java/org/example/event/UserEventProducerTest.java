package org.example.event;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.dto.UserEventDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class UserEventProducerTest {
    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka:latest"));

    @Autowired
    private UserEventProducer userEventProducerTest;

    private Consumer<String, UserEventDto> consumerTest;

    @DynamicPropertySource
    public static void configureTestProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        dynamicPropertyRegistry.add("kafka.topic.user-events", () -> "user-events-test");
    }

    @BeforeEach
    public void setUp() {
        Map<String, Object> properties = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, "test-group",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class
        );
        JacksonJsonDeserializer<UserEventDto> jsonDeserializer = new JacksonJsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        consumerTest = new DefaultKafkaConsumerFactory<String, UserEventDto>
                (properties, new StringDeserializer(), jsonDeserializer).createConsumer();
        consumerTest.subscribe(List.of("user-events-test"));
    }

    @AfterEach
    public void shutDown() {
        if (consumerTest != null) consumerTest.close();
    }

    @Test
    public void sendEvent_whenOk_sendsEventToKafka() {
        assertDoesNotThrow(() -> userEventProducerTest.sendEvent("CREATED", "Test", "test@test.com"));
        ConsumerRecords<String, UserEventDto> consumerRecordsTest = consumerTest.poll(Duration.ofSeconds(10));
        assertFalse(consumerRecordsTest.isEmpty());
        UserEventDto userEventDtoTest = consumerRecordsTest.iterator().next().value();
        assertEquals("CREATED", userEventDtoTest.getEvent());
        assertEquals("Test", userEventDtoTest.getUserName());
        assertEquals("test@test.com", userEventDtoTest.getUserEmail());
    }
}
