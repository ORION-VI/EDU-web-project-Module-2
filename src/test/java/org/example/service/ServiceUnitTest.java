package org.example.service;

import org.example.dao.DataAccessObject;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceUnitTest {
    private String name;
    private String email;
    private Integer age;
    private Long id;

    @Mock
    private DataAccessObject dataAccessObjectMock;

    @InjectMocks
    private Service testingService;

    @BeforeEach
    public void setUp() {
        name = "Test";
        email = "test@test.com";
        age = 25;
        id = 1L;
    }

    @Test
    public void findUser_whenExistingIdProvided_resultTrue() {
        User testUser = User.buildUser(name, email, age);
        testUser.setId(id);
        when(dataAccessObjectMock.findUser(id)).thenReturn(testUser);
        User testResult = testingService.findUser(id);
        assertEquals(testUser, testResult);
        verify(dataAccessObjectMock).findUser(id);
    }
    @Test
    public void findUser_whenUserNotFound_resultNull() {
        when(dataAccessObjectMock.findUser(id)).thenReturn(null);
        User testResult = testingService.findUser(id);
        assertNull(testResult);
        verify(dataAccessObjectMock).findUser(id);
    }
    @Test
    public void findUser_whenInvalidId_noResult() {
        User testResult = testingService.findUser(-1L);
        verify(dataAccessObjectMock, never()).findUser(any());
    }

    @Test
    public void saveUser_whenInputIsCorrect_userPassedToDaoMethod() {
        testingService.saveUser(name, email, age);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(dataAccessObjectMock).saveUser(userCaptor.capture());
        User testUser = userCaptor.getValue();
        assertEquals(name, testUser.getName());
        assertEquals(email, testUser.getEmail());
        assertEquals(age, testUser.getAge());
    }
    @Test
    public void saveUser_whenInvalidName_noResult() {
        testingService.saveUser(null, email, age);
        verify(dataAccessObjectMock, never()).saveUser(any());
    }
    @Test
    public void saveUser_whenInvalidEmail_noResult() {
        testingService.saveUser(name, "testtest.com", age);
        verify(dataAccessObjectMock, never()).saveUser(any());
    }
    @Test
    public void saveUser_whenInvalidAge_noResult() {
        testingService.saveUser(name, email, -25);
        verify(dataAccessObjectMock, never()).saveUser(any());
    }

    @Test
    public void updateUser_whenInputIsCorrect_userPassedToDaoMethod() {
        testingService.updateUser(id, name, email, age);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(dataAccessObjectMock).updateUser(userCaptor.capture());
        User testUser = userCaptor.getValue();
        assertEquals(id, testUser.getId());
        assertEquals(name, testUser.getName());
        assertEquals(email, testUser.getEmail());
        assertEquals(age, testUser.getAge());
    }
    @Test
    public void updateUser_whenInvalidId_noResult() {
        testingService.updateUser(-1L, name, email, age);
        verify(dataAccessObjectMock, never()).updateUser(any());
    }
    @Test
    public void updateUser_whenInvalidName_noResult() {
        testingService.updateUser(id, null, email, age);
        verify(dataAccessObjectMock, never()).updateUser(any());
    }
    @Test
    public void updateUser_whenInvalidEmail_noResult() {
        testingService.updateUser(id, name, "testtest.com", age);
        verify(dataAccessObjectMock, never()).updateUser(any());
    }
    @Test
    public void updateUser_whenInvalidAge_noResult() {
        testingService.updateUser(id, name, email, -25);
        verify(dataAccessObjectMock, never()).updateUser(any());
    }

    @Test
    public void deleteUser_whenCorrectId_idPassedToDaoMethod() {
        testingService.deleteUser(id);
        verify(dataAccessObjectMock).deleteUser(id);
    }
    @Test
    public void deleteUser_whenInvalidId_noResult() {
        testingService.deleteUser(-1L);
        verify(dataAccessObjectMock, never()).deleteUser(any());
    }

    @Test
    public void findAllUsers_daoMethodInvoked() {
        testingService.findAllUsers();
        verify(dataAccessObjectMock).findAllUsers();
    }
}
