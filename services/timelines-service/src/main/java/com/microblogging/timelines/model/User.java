package com.microblogging.timelines.model;

import java.time.Instant;

public record User(
        Long id,
        String username,
        Instant createdAt
) { }
