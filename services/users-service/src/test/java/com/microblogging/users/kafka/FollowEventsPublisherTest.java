package com.microblogging.users.kafka;

import com.microblogging.users.kafka.event.FollowEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static com.microblogging.users.kafka.FollowEventsPublisher.FOLLOW_TOPIC;
import static com.microblogging.users.kafka.FollowEventsPublisher.UNFOLLOW_TOPIC;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FollowEventsPublisherTest {

    @Mock
    private KafkaTemplate<String, FollowEvent> kafkaTemplate;

    @InjectMocks
    private FollowEventsPublisher followEventsPublisher;

    @Test
    public void testSendFollowEvent_Ok() {
        FollowEvent expectedEvent = new FollowEvent(1L, 2L);
        followEventsPublisher.sendFollowEvent(1L, 2L);
        verify(kafkaTemplate, times(1)).send(eq(FOLLOW_TOPIC), refEq(expectedEvent));
    }

    @Test
    public void testSendUnfollowEvent_Ok() {
        FollowEvent expectedEvent = new FollowEvent(1L, 2L);
        followEventsPublisher.sendUnfollowEvent(1L, 2L);
        verify(kafkaTemplate, times(1)).send(eq(UNFOLLOW_TOPIC), refEq(expectedEvent));
    }

}
