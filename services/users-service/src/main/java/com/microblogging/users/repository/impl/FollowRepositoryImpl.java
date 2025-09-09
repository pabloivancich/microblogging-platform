package com.microblogging.users.repository.impl;

import com.microblogging.users.model.User;
import com.microblogging.users.repository.FollowRepository;
import com.microblogging.users.repository.mapper.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowRepositoryImpl implements FollowRepository {

    private final JdbcTemplate jdbcTemplate;

    private final UserRowMapper userRowMapper;

    public FollowRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = new UserRowMapper();
    }

    /**
     * Creates a relationship of follower-followed
     * @param followerId
     * @param followedId
     */
    @Override
    public int follow(Long followerId, Long followedId) {
        return jdbcTemplate.update(
            "INSERT INTO follows (follower_id, followee_id) VALUES (?, ?) ON CONFLICT DO NOTHING",
            followerId,
            followedId
        );
    }

    /**
     * Deletes a relationship of follower-followed
     */
    @Override
    public int unfollow(Long followerId, Long followedId) {
        return jdbcTemplate.update(
            "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?",
            followerId,
            followedId
        );
    }

    /**
     * Returns a list of users who follows a given user ID (followed).
     */
    @Override
    public List<User> getFollowers(Long userId) {
        return jdbcTemplate.query(
            "SELECT u.* FROM follows f JOIN users u ON f.follower_id = u.id WHERE f.followee_id = ?",
            userRowMapper,
            userId
        );
    }

    /**
     * Returns a list of users who are followed by a given user ID (follower).
     */
    @Override
    public List<User> getFollowees(Long userId) {
        return jdbcTemplate.query(
            "SELECT u.* FROM follows f JOIN users u ON f.followee_id = u.id WHERE f.follower_id = ?",
            userRowMapper,
            userId
        );
    }

}
