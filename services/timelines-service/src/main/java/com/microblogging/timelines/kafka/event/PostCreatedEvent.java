package com.microblogging.timelines.kafka.event;

import java.time.Instant;
import java.util.UUID;

public record PostCreatedEvent(
        Long userId,
        UUID postId,
        String content,
        Instant createdAt
){}
