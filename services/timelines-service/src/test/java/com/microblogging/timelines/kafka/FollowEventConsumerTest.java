package com.microblogging.timelines.kafka;

import com.microblogging.timelines.kafka.event.FollowEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class FollowEventConsumerTest {

    @InjectMocks
    private FollowEventConsumer followEventConsumer;

    @Test
    public void testHandleUserFollowed_Ok() {
        FollowEvent event = new FollowEvent(1L, 2L);
        followEventConsumer.handleUserFollowed(event);
        // Nothing to test because method do nothing for this MVP
    }

    @Test
    public void testHandleUserUnfollowed_Ok() {
        FollowEvent event = new FollowEvent(1L, 2L);
        followEventConsumer.handleUserUnfollowed(event);
        // Nothing to test because method do nothing for this MVP
    }

}
