package org.example.api;

import org.example.dto.EmailRequestDto;
import org.example.service.UserEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    UserEmailService userEmailServiceTest;

    @Test
    public void sendEmailManually_whenOk_emailSent() {
        EmailRequestDto emailRequestDto = new EmailRequestDto("test@test.com", "Test subject", "Test body");
        String testRequest = objectMapper.writeValueAsString(emailRequestDto);
        assertDoesNotThrow(() -> mockMvc.perform(post("/api/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sending was successful!")));
    }

    @Test
    public void sendEmailManually_whenInvalidRequest_badRequest() throws Exception {
        EmailRequestDto emailRequestDto = new EmailRequestDto("testtest.com", null, null);
        String testRequest = objectMapper.writeValueAsString(emailRequestDto);
        mockMvc.perform(post("/api/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sendEmailManually_whenSendingFailed_internalServerError() throws Exception {
        EmailRequestDto emailRequestDto = new EmailRequestDto("test@test.com", "Test subject", "Test body");
        String testRequest = objectMapper.writeValueAsString(emailRequestDto);
        doThrow(RuntimeException.class).when(userEmailServiceTest).sendEmail(any(), any(), any());
        mockMvc.perform(post("/api/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Oops! Something happened. We'll fix it soon!"));
    }
}