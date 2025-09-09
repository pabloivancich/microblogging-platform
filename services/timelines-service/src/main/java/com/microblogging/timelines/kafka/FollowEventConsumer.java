package com.microblogging.timelines.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.timelines.kafka.event.FollowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FollowEventConsumer {

    public static final String FOLLOW_TOPIC = "user.follow";
    public static final String UNFOLLOW_TOPIC = "user.unfollow";

    private final ObjectMapper objectMapper;

    @Autowired
    public FollowEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = FOLLOW_TOPIC, groupId = "timelines-service")
    public void handleUserFollowed(String message) throws JsonProcessingException {
        FollowEvent event = objectMapper.readValue(message, FollowEvent.class);
        log.info("Consuming user follow event with follower={} and followee={}", event.followerId(), event.followeeId());
        // Backfill last N posts of followed user into follower’s timeline.
        // This will not be part of the MVP.
    }

    @KafkaListener(topics = UNFOLLOW_TOPIC, groupId = "timelines-service")
    public void handleUserUnfollowed(String message) throws JsonProcessingException {
        FollowEvent event = objectMapper.readValue(message, FollowEvent.class);
        log.info("Consuming user unfollow event with follower={} and followee={}", event.followerId(), event.followeeId());
        // Remove last N posts of unfollowed user from follower timeline.
        // This will not be part of the MVP.
    }

}
