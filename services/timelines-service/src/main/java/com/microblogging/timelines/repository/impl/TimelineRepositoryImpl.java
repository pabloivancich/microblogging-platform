package com.microblogging.timelines.repository.impl;

import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.repository.TimelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Repository for handling timeline data in Redis.
 * It provides an abstraction layer over the raw Redis operations.
 */
@Repository
public class TimelineRepositoryImpl implements TimelineRepository {
    private static final int MAX_TIMELINE_SIZE = 100;

    private final RedisTemplate<String, Post> redisTemplate;
    private final ZSetOperations<String, Post> zSetOperations;

    @Autowired
    public TimelineRepositoryImpl(RedisTemplate<String, Post>  redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * Adds a post to a user's timeline.
     * The timeline is a sorted set where the score is the timestamp.
     */
    public void addToTimeline(Long userId, Post post, long timestamp) {
        String timelineKey = "timeline:" + userId;
        zSetOperations.add(timelineKey, post, timestamp);
        // Trim the timeline to the maximum size.
        zSetOperations.removeRange(timelineKey, 0, -MAX_TIMELINE_SIZE - 1);
    }

    /**
     * Retrieves the latest posts for a user's timeline.
     *
     * @param userId the user's ID.
     * @return a list of post IDs.
     */
    public List<Post> getTimeline(final Long userId) {
        String timelineKey = "timeline:" + userId;
        Set<Post> postsSet = zSetOperations.reverseRange(timelineKey, 0, MAX_TIMELINE_SIZE - 1);
        List<Post> posts = new ArrayList<>();
        if (postsSet != null) {
            posts.addAll(postsSet);
        }

        return posts;
    }

    /**
     * Checks if a user's timeline exists in Redis.
     *
     * @param userId the user's ID.
     * @return true if the timeline key exists, false otherwise.
     */
    public boolean timelineExists(Long userId) {
        String timelineKey = "timeline:" + userId;
        return redisTemplate.hasKey(timelineKey);
    }

}
