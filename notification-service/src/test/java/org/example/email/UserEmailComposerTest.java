package org.example.email;

import org.example.dto.UserEventDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEmailComposerTest {

    private final UserEmailComposer userEmailComposerTest = new UserEmailComposer();

    @Test
    public void composeByEvent_whenUserCreated_returnsEmailObject() {
        UserEventDto userEventDtoTest = UserEventDto.buildUserEventDto
                ("CREATED", "Test", "test@test.com");
        Email emailTest = assertDoesNotThrow(() -> userEmailComposerTest.composeByEvent(userEventDtoTest));
        assertNotNull(emailTest);
        assertNotNull(emailTest.getSubject());
        assertNotNull(emailTest.getBody());
        assertTrue(emailTest.getSubject().contains("joining"));
        assertTrue(emailTest.getBody().contains("created"));
    }

    @Test
    public void composeByEvent_whenUserDeleted_returnsEmailObject() {
        UserEventDto userEventDtoTest = UserEventDto.buildUserEventDto
                ("DELETED", "Test", "test@test.com");
        Email emailTest = assertDoesNotThrow(() -> userEmailComposerTest.composeByEvent(userEventDtoTest));
        assertNotNull(emailTest);
        assertNotNull(emailTest.getSubject());
        assertNotNull(emailTest.getBody());
        assertTrue(emailTest.getSubject().contains("soon"));
        assertTrue(emailTest.getBody().contains("deleted"));
    }

    @Test
    public void composeByEvent_whenInvalidEvent_throwsIllegalArgumentException() {
        UserEventDto userEventDtoTest = UserEventDto.buildUserEventDto
                ("TEST", "Test", "test@test.com");
        assertThrowsExactly(IllegalArgumentException.class, () -> userEmailComposerTest.composeByEvent(userEventDtoTest));
    }
}
