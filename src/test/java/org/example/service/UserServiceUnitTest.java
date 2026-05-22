package org.example.service;

import org.example.dao.UserRepository;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    private String name;
    private String email;
    private Integer age;
    private Long id;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userServiceTest;

    @BeforeEach
    public void setUp() {
        name = "Test";
        email = "test@test.com";
        age = 25;
        id = 1L;
    }

    @Test
    public void findUser_whenUserExists_returnsUser() {
        User testUser = User.buildUser(name, email, age);
        testUser.setId(id);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(testUser));
        User testResult = userServiceTest.findUser(id);
        assertEquals(testUser, testResult);
        verify(userRepositoryMock).findById(id);
    }

    @Test
    public void findUser_whenUserNotFound_noResult() {
        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());
        assertThrowsExactly(RuntimeException.class, () -> userServiceTest.findUser(id));
        verify(userRepositoryMock).findById(id);
    }

    @Test
    public void findUser_whenInvalidId_noResult() {
        assertThrowsExactly(IllegalArgumentException.class, () -> userServiceTest.findUser(-1L));
        verify(userRepositoryMock, never()).findById(any());
    }

    @Test
    public void saveUser_whenValidUser_returnsUser() {
        User testUser = User.buildUser(name, email, age);
        when(userRepositoryMock.save(testUser)).thenReturn(testUser);
        User savedUser = userServiceTest.saveUser(testUser);
        verify(userRepositoryMock).save(testUser);
        assertEquals(testUser, savedUser);
    }

    @Test
    public void saveUser_whenInvalidUser_noResult() {
        User testUser = User.buildUser(null, "testest.com", -5);
        assertThrowsExactly(IllegalArgumentException.class, () -> userServiceTest.saveUser(testUser));
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    public void updateUser_whenValidUserAndId_returnsUpdatedUser() {
        User testUser = User.buildUser(name, email, age);
        User newUser = User.buildUser("Newtest", "test2@test.com", 35);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(testUser));
        when(userRepositoryMock.save(testUser)).thenReturn(testUser);
        testUser = userServiceTest.updateUser(id, newUser);
        assertEquals(testUser, newUser);
        verify(userRepositoryMock).findById(id);
        verify(userRepositoryMock).save(testUser);
    }

    @Test
    public void updateUser_whenInvalidId_noResult() {
        User testUser = User.buildUser(name, email, age);
        assertThrowsExactly(IllegalArgumentException.class, () -> userServiceTest.updateUser(-1L, testUser));
        verify(userRepositoryMock, never()).findById(any());
        verify(userRepositoryMock, never()).save(testUser);
    }

    @Test
    public void updateUser_whenInvalidUser_noResult() {
        User testUser = User.buildUser(null, "testtest.com", -5);
        assertThrowsExactly(IllegalArgumentException.class, () -> userServiceTest.updateUser(id, testUser));
        verify(userRepositoryMock, never()).findById(id);
        verify(userRepositoryMock, never()).save(testUser);
    }

    @Test
    public void updateUser_whenUserNotFound_noResult() {
        User testUser = User.buildUser(name, email, age);
        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());
        assertThrowsExactly(RuntimeException.class, () -> userServiceTest.updateUser(id, testUser));
        verify(userRepositoryMock).findById(id);
        verify(userRepositoryMock, never()).save(testUser);
    }

    @Test
    public void deleteUser_whenValidId_isOk() {
        userServiceTest.deleteUser(id);
        verify(userRepositoryMock).deleteById(id);
    }

    @Test
    public void deleteUser_whenInvalidId_noResult() {
        assertThrowsExactly(IllegalArgumentException.class, () -> userServiceTest.deleteUser(-1L));
        verify(userRepositoryMock, never()).findById(id);
        verify(userRepositoryMock, never()).deleteById(any());
    }

    @Test
    public void deleteUser_whenUserNotFound_noResult() {
        when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());
        assertThrowsExactly(RuntimeException.class, () -> userServiceTest.deleteUser(id));
        verify(userRepositoryMock).findById(id);
        verify(userRepositoryMock, never()).deleteById(id);
    }

    @Test
    public void findAllUsers_isOk() {
        List<User> userList = new ArrayList<>();
        userList.add(User.buildUser(name, email, age));
        when(userRepositoryMock.findAll()).thenReturn(userList);
        List<User> testUserList = userServiceTest.findAllUsers();
        verify(userRepositoryMock).findAll();
        assertEquals(testUserList, userList);
    }
}
