package com.microblogging.timelines.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microblogging.timelines.config.JacksonConfig;
import com.microblogging.timelines.kafka.event.FollowEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class FollowEventConsumerTest {

    private final ObjectMapper objectMapper = JacksonConfig.buildObjectMapper();

    @InjectMocks
    private FollowEventConsumer followEventConsumer;

    @BeforeEach
    public void init() {
        followEventConsumer = new FollowEventConsumer(objectMapper);
    }

    @Test
    public void testHandleUserFollowed_Ok() throws JsonProcessingException {
        FollowEvent event = new FollowEvent(1L, 2L);
        followEventConsumer.handleUserFollowed(objectMapper.writeValueAsString(event));
        // Nothing to test because method do nothing for this MVP
    }

    @Test
    public void testHandleUserUnfollowed_Ok() throws JsonProcessingException {
        FollowEvent event = new FollowEvent(1L, 2L);
        followEventConsumer.handleUserUnfollowed(objectMapper.writeValueAsString(event));
        // Nothing to test because method do nothing for this MVP
    }

}
