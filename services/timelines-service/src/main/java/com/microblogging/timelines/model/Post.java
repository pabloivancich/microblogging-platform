package com.microblogging.timelines.model;

import java.time.Instant;
import java.util.UUID;

public record Post(
    Long userId,
    UUID postId,
    String content,
    Instant createdAt
) { }
