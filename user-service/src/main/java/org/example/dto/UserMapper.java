package org.example.dto;

import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements DtoMapperInterface<UserDto, User> {

    @Override
    public UserDto toDto(User object) {
        return new UserDto(object.getId(), object.getName(), object.getEmail(), object.getAge());
    }

    @Override
    public User toEntity(UserDto object) {
        User user = User.buildUser(object.getName(), object.getEmail(), object.getAge());
        if (object.getId() != null) user.setId(object.getId());
        return user;
    }
}
