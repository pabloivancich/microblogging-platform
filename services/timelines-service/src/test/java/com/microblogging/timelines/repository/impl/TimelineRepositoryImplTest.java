package com.microblogging.timelines.repository.impl;

import com.microblogging.timelines.config.RedisTestcontainersConfiguration;
import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.repository.TimelineRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(RedisTestcontainersConfiguration.class)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TimelineRepositoryImplTest {

    @Autowired
    private RedisTemplate<String, Post> redisTemplate;

    @Autowired
    private TimelineRepository timelineRepository;

    @Test
    @Order(1)
    void shouldAddPostToTimelineAndRetrieveIt() {
        Long userId = 123L;
        Post post = new Post(userId, UUID.randomUUID(), "Post content", Instant.now());

        timelineRepository.addToTimeline(userId, post, post.createdAt().toEpochMilli());

        List<Post> posts = timelineRepository.getTimeline(userId);

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals(post.content(), posts.getFirst().content());
        assertEquals(post.userId(), posts.getFirst().userId());
    }

    @Test
    @Order(2)
    void shouldReturnTrueIfTimelineExists() {
        Long userId = 456L;
        Post post = new Post(userId, UUID.randomUUID(), "Another post!", Instant.now());

        timelineRepository.addToTimeline(userId, post, post.createdAt().toEpochMilli());

        assertTrue(timelineRepository.timelineExists(userId));
    }

    @Test
    @Order(3)
    void shouldReturnFalseIfTimelineDoesNotExist() {
        assertFalse(timelineRepository.timelineExists(999L));
    }

    @Test
    @Order(4)
    void shouldTrimTimelineWhenExceedsMaxSize() {
        Long userId = 789L;
        for (int i = 0; i < 150; i++) {
            Post post = new Post(userId, UUID.randomUUID(), "Post " + i, Instant.now());
            timelineRepository.addToTimeline(userId, post, post.createdAt().toEpochMilli());
        }

        List<Post> posts = timelineRepository.getTimeline(userId);

        assertEquals(100, posts.size(), "Timeline should be trimmed to max 100 posts");
    }
}
