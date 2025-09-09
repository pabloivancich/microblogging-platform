package com.microblogging.timelines.controller;

import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.service.TimelineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimelineController.class)
public class TimelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimelineService timelineService;

    @Test
    public void testGetTimeline_Ok() throws Exception {
        Post postOne = new Post(1L, UUID.randomUUID(), "First post", Instant.now());
        Post postTwo = new Post(1L, UUID.randomUUID(), "Second post", Instant.now());
        List<Post> posts = List.of(postOne, postTwo);
        when(timelineService.getTimeline(anyLong())).thenReturn(posts);

        ResultActions result = mockMvc.perform(get("/timelines/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetTimeline_Empty() throws Exception {
        when(timelineService.getTimeline(anyLong())).thenReturn(List.of());

        ResultActions result = mockMvc.perform(get("/timelines/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    public void testGetTimeline_Exception() throws Exception {
        doThrow(RuntimeException.class). when(timelineService).getTimeline(anyLong());

        ResultActions result = mockMvc.perform(get("/timelines/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isInternalServerError());
    }

}
