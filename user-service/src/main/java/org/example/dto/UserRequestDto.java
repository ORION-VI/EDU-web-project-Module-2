package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequestDto {
    private final String name;
    private final String email;
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
