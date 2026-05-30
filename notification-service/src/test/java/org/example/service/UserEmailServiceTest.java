package org.example.service;

import org.example.dto.UserEventDto;
import org.example.email.Email;
import org.example.email.EmailSender;
import org.example.email.UserEmailComposer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserEmailServiceTest {

    @Mock
    public EmailSender emailSenderMock;

    @Mock
    public UserEmailComposer userEmailComposerMock;

    @InjectMocks
    public UserEmailService userEmailServiceTest;

    @Test
    public void sendEmailByEvent_whenOk_sendsEmail() {
        UserEventDto userEventDtoTest = UserEventDto.buildUserEventDto("CREATED", "Test", "test@test.com");
        Email testEmail = new Email("Test Subject", "Test Body");
        when(userEmailComposerMock.composeByEvent(userEventDtoTest)).thenReturn(testEmail);
        assertDoesNotThrow(() -> userEmailServiceTest.sendEmailByEvent(userEventDtoTest));
        verify(userEmailComposerMock).composeByEvent(userEventDtoTest);
        verify(emailSenderMock).sendEmail(userEventDtoTest.getUserEmail(), testEmail.getSubject(), testEmail.getBody());
    }

    @Test
    public void sendEmail_whenOk_sendsEmail() {
        assertDoesNotThrow(() -> userEmailServiceTest.sendEmail("test@test.com", "Test Subject", "Test Body"));
        verify(emailSenderMock).sendEmail("test@test.com", "Test Subject", "Test Body");
    }
}
