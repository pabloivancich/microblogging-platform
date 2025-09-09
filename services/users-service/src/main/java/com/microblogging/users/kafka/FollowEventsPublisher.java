package com.microblogging.users.kafka;

import com.microblogging.users.kafka.event.FollowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Service class for producing Kafka events related to follow/unfollow.
 */
@Component
@Slf4j
public class FollowEventsPublisher {

    public static final String FOLLOW_TOPIC = "user.follow";
    public static final String UNFOLLOW_TOPIC = "user.unfollow";

    private final KafkaTemplate<String, FollowEvent> kafkaTemplate;

    @Autowired
    public FollowEventsPublisher(KafkaTemplate<String, FollowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a follow event to the FOLLOW_TOPIC topic.
     * @param followerId The ID of the user who initiated the follow.
     * @param followeeId The ID of the user who is being followed.
     */
    public void sendFollowEvent(Long followerId, Long followeeId) {
        String message = String.format("User %s followed user %s", followerId.toString(), followeeId.toString());
        log.info("Sending follow event to topic {}: {}", FOLLOW_TOPIC, message);
        FollowEvent event = new FollowEvent(followerId, followeeId);
        kafkaTemplate.send(FOLLOW_TOPIC, event);
    }

    /**
     * Sends an unfollow event to the UNFOLLOW_TOPIC topic.
     * @param followerId The ID of the user who initiated to unfollow.
     * @param followeeId The ID of the user who is being unfollowed.
     */
    public void sendUnfollowEvent(Long followerId, Long followeeId) {
        String message = String.format("User %s unfollowed user %s", followerId.toString(), followeeId.toString());
        log.info("Sending unfollow event to topic {}: {}", UNFOLLOW_TOPIC, message);

        FollowEvent event = new FollowEvent(followerId, followeeId);
        kafkaTemplate.send(UNFOLLOW_TOPIC, event);
    }

}