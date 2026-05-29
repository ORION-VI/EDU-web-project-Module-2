package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserEventDto {
    private final String event;
    private final String userName;
    private final String userEmail;

    private UserEventDto(String event, String userName, String userEmail) {
        this.event = event;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getEvent() {
        return event;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    @JsonCreator
    public static UserEventDto buildUserEventDto(@JsonProperty("event") String event,
                                                 @JsonProperty("userName") String userName,
                                                 @JsonProperty("userEmail") String userEmail) {
        return new UserEventDto(event, userName, userEmail);
    }
}
