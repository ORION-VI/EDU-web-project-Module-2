package org.example.email;

import org.example.dto.UserEventDto;
import org.springframework.stereotype.Component;

@Component
public class UserEmailComposer implements EmailComposerInterface<UserEventDto> {

    public UserEmailComposer() {
    }

    @Override
    public Email composeByEvent(UserEventDto userEventDto) {
        return switch (userEventDto.getEvent()) {
            case "CREATED" -> composeWelcomeEmail(userEventDto);
            case "DELETED" -> composeFarewellEmail(userEventDto);
            default -> throw new IllegalArgumentException("FAILED TO COMPOSE EMAIL DUE TO UNKNOWN EVENT " + '"'
                    + userEventDto.getEvent() + '"');
        };
    }

    private Email composeWelcomeEmail(UserEventDto userEventDto) {
        return new Email("Congratulations on joining us!",
                """
                        Hello and welcome!
                        We're happy to announce that your account %s was successfully created!
                        Enjoy your stay in our wonderful community.
                        Toodles!
                        """.formatted(userEventDto.getUserName()));
    }

    private Email composeFarewellEmail(UserEventDto userEventDto) {
        return new Email("Hope to see you again soon!",
                """
                        Hello there!
                        Your account %s was successfully deleted!
                        We understand your decision and we're hoping to see you with us again!
                        Stay safe!
                        """.formatted(userEventDto.getUserName()));
    }
}
