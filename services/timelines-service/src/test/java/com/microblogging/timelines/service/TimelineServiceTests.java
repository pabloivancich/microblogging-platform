package com.microblogging.timelines.service;

import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.repository.TimelineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TimelineServiceTests {

    @Mock
    private TimelineRepository timelineRepository;

    @InjectMocks
    private TimelineService timelineService;

    @Test
    public void testGetTimeline_Ok() {
        Post postOne = new Post(1L, UUID.randomUUID(), "First post", Instant.now());
        Post postTwo = new Post(1L, UUID.randomUUID(), "Second post", Instant.now());
        List<Post> posts = List.of(postOne, postTwo);
        when(timelineRepository.getTimeline(anyLong())).thenReturn(posts);

        List<Post> responsePosts = timelineService.getTimeline(1L);

        Assertions.assertFalse(responsePosts.isEmpty());
        Assertions.assertEquals(2, responsePosts.size());

        verify(timelineRepository, times(1)).getTimeline(anyLong());
    }

    @Test
    public void testGetTimeline_Empty() {
        when(timelineRepository.getTimeline(anyLong())).thenReturn(List.of());

        List<Post> responsePosts = timelineService.getTimeline(1L);

        Assertions.assertTrue(responsePosts.isEmpty());

        verify(timelineRepository, times(1)).getTimeline(anyLong());
    }

    @Test
    public void testAddToTimeline_Ok() {
        Post post = new Post(1L, UUID.randomUUID(), "Post content", Instant.now());
        doNothing().when(timelineRepository).addToTimeline(anyLong(), any(Post.class), anyLong());
        timelineService.addToTimeline(1L, post);

        verify(timelineRepository, times(1)).addToTimeline(1L, post, post.createdAt().toEpochMilli());
    }

}
