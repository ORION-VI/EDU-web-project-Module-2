package org.example.mapper;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapperInterface<UserRequestDto, UserResponseDto, User> {

    @Override
    public UserResponseDto toDto(User object) {
        return UserResponseDto.buildUserDto(object.getId(), object.getName(), object.getEmail(), object.getAge());
    }

    @Override
    public User toEntity(UserRequestDto object) {
        return User.buildUser(object.getName(), object.getEmail(), object.getAge());
    }
}