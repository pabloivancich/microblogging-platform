package com.microblogging.users.controller;

import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.model.User;
import com.microblogging.users.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowController.class)
public class FollowControllerTest {

    // MockMvc is the main tool for testing Spring MVC controllers. It is automatically configured by @WebMvcTest.
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FollowService followService;


    @Test
    public void testFollow_Ok() throws Exception {
        doNothing().when(followService).follow(anyLong(), anyLong());

        ResultActions result = mockMvc.perform(
                post("/users/{followerId}/follow/{followeeId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());

        verify(followService, times(1)).follow(anyLong(), anyLong());
    }

    @Test
    public void testFollow_ValidationException() throws Exception {
        doThrow(ValidationException.class).when(followService).follow(anyLong(), anyLong());

        ResultActions result = mockMvc.perform(
                post("/users/{followerId}/follow/{followeeId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest());

        verify(followService, times(1)).follow(anyLong(), anyLong());
    }

    @Test
    public void testUnfollow_Ok() throws Exception {
        doNothing().when(followService).unfollow(anyLong(), anyLong());

        ResultActions result = mockMvc.perform(
                delete("/users/{followerId}/follow/{followeeId}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetFollowers() throws Exception {
        User user = new User(2L, "follower", Instant.now());
        when(followService.getFollowers(anyLong())).thenReturn(List.of(user));

        ResultActions result = mockMvc.perform(get("/users/{userId}/followers", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetFollowees() throws Exception {
        User user = new User(2L, "follower", Instant.now());
        when(followService.getFollowees(anyLong())).thenReturn(List.of(user));

        ResultActions result = mockMvc.perform(get("/users/{userId}/followees", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

}


