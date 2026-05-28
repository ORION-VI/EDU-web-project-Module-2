package org.example.mapper;

import org.example.dto.UserDto;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapperInterface<UserDto, User> {

    @Override
    public UserDto toDto(User object) {
        return UserDto.buildUserDto(object.getId(), object.getName(), object.getEmail(), object.getAge());
    }

    @Override
    public User toEntity(UserDto object) {
        User user = User.buildUser(object.getName(), object.getEmail(), object.getAge());
        if (object.getId() != null) user.setId(object.getId());
        return user;
    }
}