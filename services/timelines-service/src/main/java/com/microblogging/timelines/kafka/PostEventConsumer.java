package com.microblogging.timelines.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.timelines.client.UsersServiceClient;
import com.microblogging.timelines.kafka.event.PostCreatedEvent;
import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.model.User;
import com.microblogging.timelines.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PostEventConsumer {
    public final static String POST_CREATED_TOPIC = "post.created";

    private final TimelineService timelineService;
    private final UsersServiceClient usersService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PostEventConsumer(
            final TimelineService timelineService,
            final UsersServiceClient usersService,
            final ObjectMapper objectMapper
    ) {
        this.timelineService = timelineService;
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = POST_CREATED_TOPIC, groupId = "timelines-service")
    public void handlePostCreated(String message) throws JsonProcessingException {
        // Retrieve list of followers from Users service
        // This can be stored in Redis or cache in order to avoid calling Users service several times in a short time
        // if a user created a lot of posts
        PostCreatedEvent event = objectMapper.readValue(message, PostCreatedEvent.class);
        log.info("Consuming post created event from user={} with content={}", event.userId(), event.content());

        List<User> followers = usersService.getFollowers(event.userId());

        // Add post to followers timeline
        for (User follower : followers) {
            timelineService.addToTimeline(follower.id(), buildPostFromPostCreatedEvent(event));
        }
    }

    private Post buildPostFromPostCreatedEvent(PostCreatedEvent event) {
        return new Post(event.userId(), event.postId(), event.content(), event.createdAt());
    }

}