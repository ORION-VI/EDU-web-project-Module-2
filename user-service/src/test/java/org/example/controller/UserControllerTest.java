package org.example.controller;

import org.example.dto.UserMapper;
import org.example.entity.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserMapper.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userServiceTest;

    @Test
    public void getUserById_whenOk_responseWithUserDto() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        when(userServiceTest.findUser(1L)).thenReturn(testUser);
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    public void getUserById_whenUserNotFound_notFoundResponse() throws Exception {
        when(userServiceTest.findUser(1L)).thenThrow(RuntimeException.class);
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserById_whenInvalidId_badRequestResponse() throws Exception {
        when(userServiceTest.findUser(-1L)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/api/users/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsers_responseWithUserDtoList() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        User newTestUser = User.buildUser("Newtest", "test2@test.com", 25);
        List<User> testUserList = new ArrayList<>();
        testUserList.add(testUser);
        testUserList.add(newTestUser);
        when(userServiceTest.findAllUsers()).thenReturn(testUserList);
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void createUser_whenOk_returnsDtoOfCreatedUser() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        String testJsonRequest = objectMapper.writeValueAsString(testUser);
        User savedUser = User.buildUser("Test", "test@test.com", 25);
        savedUser.setId(1L);
        when(userServiceTest.saveUser(any(User.class))).thenReturn(savedUser);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    public void createUser_whenUserDataNotValid_badRequestResponse() throws Exception {
        User testUser = User.buildUser(null, "testtest.com", -2);
        String testJsonRequest = objectMapper.writeValueAsString(testUser);
        when(userServiceTest.saveUser(any(User.class))).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_whenEmailAlreadyExists_conflictResponse() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        String testJsonRequest = objectMapper.writeValueAsString(testUser);
        when(userServiceTest.saveUser(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void updateUser_whenOk_returnsDtoOfUpdatedUser() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        String testJsonRequest = objectMapper.writeValueAsString(testUser);
        User updatedTestUser = User.buildUser("Test", "test@test.com", 25);
        updatedTestUser.setId(1L);
        when(userServiceTest.updateUser(eq(1L), any(User.class))).thenReturn(updatedTestUser);
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    public void updateUser_whenInvalidId_badRequestResponse() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        String testJsonRequest = objectMapper.writeValueAsString(testUser);
        when(userServiceTest.updateUser(eq(-1L), any(User.class))).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(put("/api/users/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUser_whenUserNotFound_notFoundResponse() throws Exception {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        String testJsonRequest = objectMapper.writeValueAsString(testUser);
        when(userServiceTest.updateUser(eq(1L), any(User.class))).thenThrow(RuntimeException.class);
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_whenOk_okResponse() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_whenInvalidId_badRequestResponse() throws Exception {
        doThrow(IllegalArgumentException.class).when(userServiceTest).deleteUser(-1L);
        mockMvc.perform(delete("/api/users/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteUser_whenUserNotFound_notFoundResponse() throws Exception {
        doThrow(RuntimeException.class).when(userServiceTest).deleteUser(1L);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}
