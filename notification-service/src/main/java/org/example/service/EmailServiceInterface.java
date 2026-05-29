package org.example.service;

import org.example.dto.UserEventDto;

public interface EmailServiceInterface {

    void sendEmailByEvent(UserEventDto userEventDto);
    void sendEmail(String email, String subject, String body);
}
