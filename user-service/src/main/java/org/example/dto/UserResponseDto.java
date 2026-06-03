package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDto {

    @Schema(description = "Digit ID value of a user", example = "202")
    private final Long id;

    @Schema(description = "Name of a user", example = "Jack")
    private final String name;

    @Schema(description = "Email of a user", example = "wahoo@inbox.com")
    private final String email;

    @Schema(description = "Age of a user", example = "32")
    private final Integer age;

    private UserResponseDto(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    @JsonCreator
    public static UserResponseDto buildUserDto(@JsonProperty("id") Long id,
                                               @JsonProperty("name") String name,
                                               @JsonProperty("email") String email,
                                               @JsonProperty("age") Integer age) {
        return new UserResponseDto(id, name, email, age);
    }
}
