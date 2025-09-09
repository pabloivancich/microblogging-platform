package com.microblogging.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.users.controller.dto.CreateUserRequest;
import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.model.User;
import com.microblogging.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    // MockMvc is the main tool for testing Spring MVC controllers. It is automatically configured by @WebMvcTest.
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    // ObjectMapper to convert Java objects to JSON strings for request bodies.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateUserOk() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("newUser");
        String createUserRequestJson = objectMapper.writeValueAsString(createUserRequest);

        ResultActions result = mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestJson));

        result.andExpect(status().isCreated());

        verify(userService, times(1)).createUser(anyString());
    }

    @Test
    public void testCreateUserUsernameInvalid() throws Exception {
        String longUsername = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        CreateUserRequest createUserRequest = new CreateUserRequest(longUsername);
        String createUserRequestJson = objectMapper.writeValueAsString(createUserRequest);

        doThrow(ValidationException.class).when(userService).createUser(anyString());

        ResultActions result = mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestJson));

        result.andExpect(status().isBadRequest());

        verify(userService, times(1)).createUser(anyString());
    }

    @Test
    public void testCreateUserRuntimeException() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("newUser");
        String createUserRequestJson = objectMapper.writeValueAsString(createUserRequest);

        doThrow(RuntimeException.class).when(userService).createUser(anyString());

        ResultActions result = mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequestJson));

        result.andExpect(status().isInternalServerError());

        verify(userService, times(1)).createUser(anyString());
    }

    @Test
    public void testGetUserFound() throws Exception {
        User user = new User(1L, "newUser", Instant.now());
        when(userService.getUser(anyLong())).thenReturn(Optional.of(user));

        ResultActions result = mockMvc.perform(get("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        when(userService.getUser(anyLong())).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(get("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetUsersOk() throws Exception {
        when(userService.getUsers()).thenReturn(List.of());

        ResultActions result = mockMvc.perform(get("/users/", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserOk() throws Exception {
        doNothing().when(userService).removeUser(anyLong());

        ResultActions result = mockMvc.perform(delete("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

}
