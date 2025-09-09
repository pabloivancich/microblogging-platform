package com.microblogging.posts.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("posts")
@Data
public class Post {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    private final Long userId;

    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordinal = 0)
    private final Instant createdAt;

    @PrimaryKeyColumn(name = "post_id", type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    private final UUID postId;

    private final String content;

    public Post(final Long userId, final String content) {
        this.userId = userId;
        this.createdAt = Instant.now();
        this.postId = UUID.randomUUID();
        this.content = content;
    }

    public Post(final Long userId, final UUID postId, final Instant createdAt, final String content) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.postId = postId;
        this.content = content;
    }

}
