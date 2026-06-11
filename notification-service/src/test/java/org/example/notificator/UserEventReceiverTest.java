package org.example.notificator;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.dto.UserEventDto;
import org.example.email.EmailSender;
import org.example.service.EmailServiceInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserEventReceiverTest {
    private KafkaTemplate<String, UserEventDto> kafkaTemplate;

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka:latest"));

    @MockitoSpyBean
    private UserEventReceiver userEventReceiverTest;

    @MockitoSpyBean
    private EmailServiceInterface userEmailServiceTest;

    @MockitoBean
    private EmailSender emailSenderTest;

    @DynamicPropertySource
    public static void configureTestProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        dynamicPropertyRegistry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        dynamicPropertyRegistry.add("spring.kafka.consumer.group-id", () -> "notification-service-group");
        dynamicPropertyRegistry.add("spring.kafka.consumer.key-deserializer",
                () -> "org.apache.kafka.common.serialization.StringDeserializer");
        dynamicPropertyRegistry.add("spring.kafka.consumer.value-deserializer",
                () -> "org.springframework.kafka.support.serializer.JacksonJsonDeserializer");
        dynamicPropertyRegistry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "*");
        dynamicPropertyRegistry.add("spring.kafka.consumer.properties.spring.json.value.default.type",
                () -> "org.example.dto.UserEventDto");
    }

    @BeforeEach
    public void setUp() {
        Map<String, Object> properties = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class
        );
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
    }

    @AfterEach
    public void shutDown() {
        if (kafkaTemplate != null) kafkaTemplate.destroy();
    }

    @Test
    public void sendEmailAuto_whenOk_sendsEmail() {
        UserEventDto userEventDto = UserEventDto.buildUserEventDto("CREATED", "Test", "test@test.com");
        kafkaTemplate.send("user-events", userEventDto.getUserEmail(), userEventDto);
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            ArgumentCaptor<UserEventDto> captor = ArgumentCaptor.forClass(UserEventDto.class);
            verify(userEmailServiceTest).sendEmailByEvent(captor.capture());
            UserEventDto capturedUserEventDto = captor.getValue();
            assertEquals(userEventDto.getEvent(), capturedUserEventDto.getEvent());
            assertEquals(userEventDto.getUserName(), capturedUserEventDto.getUserName());
            assertEquals(userEventDto.getUserEmail(), capturedUserEventDto.getUserEmail());
            verify(userEventReceiverTest).sendEmailAuto(capturedUserEventDto);
        });
    }
}