package com.microblogging.timelines.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.timelines.client.UsersServiceClient;
import com.microblogging.timelines.config.JacksonConfig;
import com.microblogging.timelines.kafka.event.PostCreatedEvent;
import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.model.User;
import com.microblogging.timelines.service.TimelineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
public class PostEventConsumerTest {

    @Mock
    private TimelineService timelineService;

    @Mock
    private UsersServiceClient usersService;

    private final ObjectMapper objectMapper = JacksonConfig.buildObjectMapper();

    private PostEventConsumer postEventConsumer;

    @BeforeEach
    public void init() {
        postEventConsumer = new PostEventConsumer(timelineService, usersService, objectMapper);
    }

    @Test
    public void testHandlePostCreated_Ok() throws JsonProcessingException {
        Long userId = 1L;
        UUID postId = UUID.randomUUID();
        String postContent = "Hello world";
        Instant createdAt = Instant.now();
        PostCreatedEvent event = new PostCreatedEvent(userId, postId, postContent, createdAt);

        List<User> followers = List.of(
                new User(10L, "userOne", Instant.now()),
                new User(11L, "userTwo", Instant.now())
        );

        when(usersService.getFollowers(userId)).thenReturn(followers);
        doNothing().when(timelineService).addToTimeline(anyLong(), any(Post.class));

        String eventStr = objectMapper.writeValueAsString(event);
        postEventConsumer.handlePostCreated(eventStr);

        verify(usersService, times(1)).getFollowers(userId);

        ArgumentCaptor<Long> followerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        verify(timelineService, times(2))
                .addToTimeline(followerIdCaptor.capture(), postCaptor.capture());

        // assert that timeline was updated for correct followers
        Assertions.assertEquals(2, followerIdCaptor.getAllValues().size());
        Assertions.assertTrue(followerIdCaptor.getAllValues().contains(10L));
        Assertions.assertTrue(followerIdCaptor.getAllValues().contains(11L));

        // assert post was built correctly
        Post capturedPost = postCaptor.getValue();
        Assertions.assertEquals(userId, capturedPost.userId());
        Assertions.assertEquals(postId, capturedPost.postId());
        Assertions.assertEquals(postContent, capturedPost.content());
        Assertions.assertEquals(createdAt, capturedPost.createdAt());
    }

}
