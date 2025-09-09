package com.microblogging.posts.kafka;

import com.microblogging.posts.kafka.event.PostCreatedEvent;
import com.microblogging.posts.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;

import static com.microblogging.posts.kafka.PostEventsPublisher.POST_CREATED_TOPIC;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostEventsPublisherTest {

    @Mock
    private KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;

    @InjectMocks
    private PostEventsPublisher publisher;

    @Test
    public void testSendPostCreatedEvent_Ok() {
        Long userId = 1L;
        UUID postId = UUID.randomUUID();
        String content = "Testing publisher";
        Instant createdAt = Instant.now();

        Post post = new Post(userId, postId, createdAt, content);
        PostCreatedEvent expectedPostCreatedEvent = new PostCreatedEvent(userId, postId, content, createdAt);

        publisher.sendPostCreatedEvent(post);

        verify(kafkaTemplate, times(1)).send(eq(POST_CREATED_TOPIC), refEq(expectedPostCreatedEvent));
    }

}
