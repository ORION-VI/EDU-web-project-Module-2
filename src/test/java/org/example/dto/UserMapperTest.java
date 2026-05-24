package org.example.dto;

import org.example.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {
    private final static UserMapper userMapperTest = new UserMapper();

    @Test
    public void toDto_returnsUserDto() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        UserDto testUserDto = userMapperTest.toDto(testUser);
        assertNotNull(testUserDto);
        assertEquals(testUser.getName(), testUserDto.getName());
        assertEquals(testUser.getEmail(), testUserDto.getEmail());
        assertEquals(testUser.getAge(), testUserDto.getAge());
    }

    @Test
    public void toEntity_returnsUser() {
        UserDto testUserDto = new UserDto(1L, "Test", "test@test.com", 25);
        User testUser = userMapperTest.toEntity(testUserDto);
        assertNotNull(testUser);
        assertEquals(testUser.getName(), testUserDto.getName());
        assertEquals(testUser.getEmail(), testUserDto.getEmail());
        assertEquals(testUser.getAge(), testUserDto.getAge());
    }
}
