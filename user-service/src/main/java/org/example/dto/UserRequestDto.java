package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequestDto {
    @Schema(description = "Name of a user", example = "Jack", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String name;

    @Schema(description = "Email of a user", example = "toasty@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String email;

    @Schema(description = "Age of a user", example = "25", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer age;

    private UserRequestDto(String name, String email, Integer age) {
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

    @JsonCreator
    public static UserRequestDto buildUserDto(@JsonProperty("name") String name,
                                              @JsonProperty("email") String email,
                                              @JsonProperty("age") Integer age) {
        return new UserRequestDto(name, email, age);
    }
}
