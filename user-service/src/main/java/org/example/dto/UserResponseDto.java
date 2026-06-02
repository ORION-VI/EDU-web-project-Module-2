package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseDto {
    private final Long id;
    private final String name;
    private final String email;
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
