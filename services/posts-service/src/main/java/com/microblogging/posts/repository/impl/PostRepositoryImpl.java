package com.microblogging.posts.repository.impl;

import com.microblogging.posts.model.Post;
import com.microblogging.posts.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final CqlTemplate cqlTemplate;

    private final RowMapper<Post> postRowMapper = (row, i) -> {
        UUID postId = row.get("post_id", UUID.class);
        Long userId = row.get("user_id", Long.class);
        String content = row.get("content", String.class);
        Instant createdAt = row.getInstant("created_at");
        return new Post(userId, postId, createdAt, content);
    };

    @Autowired
    public PostRepositoryImpl(CqlTemplate cqlTemplate) {
        this.cqlTemplate = cqlTemplate;
    }

    /**
     * Save a new post.
     * @param post
     */
    @Override
    public void save(Post post) {
        String cql = "INSERT INTO posts_keyspace.posts (user_id, post_id, content, created_at) VALUES (?, ?, ?, ?)";
        cqlTemplate.execute(cql, post.getUserId(), post.getPostId(), post.getContent(), post.getCreatedAt());
    }

    /**
     * Retrieves all posts for a given user ID.
     * Assumes a secondary index exists on the 'user_id' column for efficient lookup.
     */
    @Override
    public List<Post> findByUserId(Long userId) {
        String cql = "SELECT * FROM posts_keyspace.posts WHERE user_id = ?";
        return cqlTemplate.query(cql, postRowMapper, userId);
    }

}
