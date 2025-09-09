package com.microblogging.timelines.kafka;

import com.microblogging.timelines.kafka.event.FollowEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FollowEventConsumer {

    @KafkaListener(topics = "user.follow", groupId = "timelines-service")
    public void handleUserFollowed(FollowEvent event) {
        // Backfill last N posts of followed user into follower’s timeline.
        // This will not be part of the MVP.
    }

    @KafkaListener(topics = "user.unfollow", groupId = "timelines-service")
    public void handleUserUnfollowed(FollowEvent event) {
        // Remove last N posts of unfollowed user from follower timeline.
        // This will not be part of the MVP.
    }

}
