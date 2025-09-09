package com.microblogging.posts.kafka;

import com.microblogging.posts.kafka.event.PostCreatedEvent;
import com.microblogging.posts.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Service class for producing Kafka events related to posts actions.
 */
@Component
@Slf4j
public class PostEventsPublisher {

    public final static String POST_CREATED_TOPIC = "post.created";

    private final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate;

    @Autowired
    public PostEventsPublisher(final KafkaTemplate<String, PostCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a post created event to the POST_CREATED_TOPIC topic.
     * @param post created.
     */
    public void sendPostCreatedEvent(Post post) {
        PostCreatedEvent postCreatedEvent = new PostCreatedEvent(
                post.getUserId(),
                post.getPostId(),
                post.getContent(),
                post.getCreatedAt()
        );
        kafkaTemplate.send(POST_CREATED_TOPIC, postCreatedEvent);
    }

}