package com.microblogging.posts.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.posts.controller.dto.CreatePostRequest;
import com.microblogging.posts.exception.ValidationException;
import com.microblogging.posts.model.Post;
import com.microblogging.posts.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    // MockMvc is the main tool for testing Spring MVC controllers. It is automatically configured by @WebMvcTest.
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreatePost_Ok() throws Exception {
        doNothing().when(postService).createPost(anyLong(), anyString());

        CreatePostRequest createPostRequest = new CreatePostRequest(1L,"content");
        String createPostRequestJson = objectMapper.writeValueAsString(createPostRequest);

        ResultActions result = mockMvc.perform(
                post("/posts/" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostRequestJson)
        );

        result.andExpect(status().isCreated());

        verify(postService, times(1)).createPost(1L, "content");
    }

    @Test
    public void testCreatePost_ValidationException() throws Exception {
        doThrow(ValidationException.class).when(postService).createPost(anyLong(), anyString());

        CreatePostRequest createPostRequest = new CreatePostRequest(1L,"invalid_content");
        String createPostRequestJson = objectMapper.writeValueAsString(createPostRequest);

        ResultActions result = mockMvc.perform(
                post("/posts/" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostRequestJson)
        );

        result.andExpect(status().isBadRequest());

        verify(postService, times(1)).createPost(anyLong(),anyString());
    }

    @Test
    public void testCreatePost_RuntimeException() throws Exception {
        doThrow(RuntimeException.class).when(postService).createPost(anyLong(), anyString());

        CreatePostRequest createPostRequest = new CreatePostRequest(1L,"content");
        String createPostRequestJson = objectMapper.writeValueAsString(createPostRequest);

        ResultActions result = mockMvc.perform(
                post("/posts/" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostRequestJson)
        );

        result.andExpect(status().isInternalServerError());

        verify(postService, times(1)).createPost(anyLong(),anyString());

    }

    @Test
    public void testGetPosts_Ok() throws Exception {
        Post postOne = new Post(1L, "content one");
        Post postTwo = new Post(1L, "content two");
        when(postService.getPostsByUserId(1L)).thenReturn(List.of(postOne, postTwo));

        ResultActions result = mockMvc.perform(get("/posts/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        verify(postService, times(1)).getPostsByUserId(1L);
    }

}
