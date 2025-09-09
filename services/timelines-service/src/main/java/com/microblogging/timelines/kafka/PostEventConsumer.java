package com.microblogging.timelines.kafka;

import com.microblogging.timelines.client.UsersServiceClient;
import com.microblogging.timelines.kafka.event.PostCreatedEvent;
import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.model.User;
import com.microblogging.timelines.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostEventConsumer {

    private final TimelineService timelineService;
    private final UsersServiceClient usersService; // to get followers list

    @Autowired
    public PostEventConsumer(final TimelineService timelineService, final UsersServiceClient usersService) {
        this.timelineService = timelineService;
        this.usersService = usersService;
    }

    @KafkaListener(topics = "posts.created", groupId = "timelines-service")
    public void handlePostCreated(PostCreatedEvent event) {
        // Retrieve list of followers from Users service
        // This can be stored in Redis or cache in order to avoid calling Users service several times in a short time
        // if a user created a lot of posts
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