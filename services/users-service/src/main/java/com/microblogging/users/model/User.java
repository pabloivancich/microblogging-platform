package com.microblogging.users.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class User {

    private final Long id;

    private final String username;

    private final Instant createdAt;

    public User(final Long id, final String username, final Instant createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }

}
