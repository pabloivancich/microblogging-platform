package com.microblogging.users.repository.impl;

import com.microblogging.users.config.PostgreSQLTestcontainersConfiguration;
import com.microblogging.users.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@JdbcTest
@Import({FollowRepositoryImpl.class, PostgreSQLTestcontainersConfiguration.class})
public class FollowRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FollowRepositoryImpl repository;

    @Test
    public void testFollowOk() {
        int followed = repository.follow(1L, 2L);
        Assertions.assertEquals(1, followed);
    }

    @Test
    public void testFollowIdempotent() {
        // 1st call
        int followed = repository.follow(2L, 1L);
        Assertions.assertEquals(1, followed);
        // 2nd call, should be ok
        followed = repository.follow(2L, 1L);
        Assertions.assertEquals(0, followed);
    }

    @Test
    public void testUnfollow() {
        int unfollowed = repository.unfollow(1L, 2L);
        Assertions.assertEquals(0, unfollowed);

        int followed = repository.follow(1L, 2L);
        Assertions.assertEquals(1, followed);

        unfollowed = repository.unfollow(1L, 2L);
        Assertions.assertEquals(1, unfollowed);
    }

    @Test
    public void testUnfollowIdempotent() {
        int unfollowed = repository.unfollow(1L, 2L);
        Assertions.assertEquals(0, unfollowed);

        int followed = repository.follow(1L, 2L);
        Assertions.assertEquals(1, followed);

        unfollowed = repository.unfollow(1L, 2L);
        Assertions.assertEquals(1, unfollowed);

        unfollowed = repository.unfollow(1L, 2L);
        Assertions.assertEquals(0, unfollowed);
    }

    @Test
    public void getGetFollowers() {
        int followed = repository.follow(7L, 10L);
        Assertions.assertEquals(1, followed);

        followed = repository.follow(8L, 10L);
        Assertions.assertEquals(1, followed);

        List<User> followers = repository.getFollowers(10L);
        Assertions.assertFalse(followers.isEmpty());
        Assertions.assertEquals(2, followers.size());
    }

    @Test
    public void testGetFollowees() {
        int followed = repository.follow(7L, 10L);
        Assertions.assertEquals(1, followed);

        followed = repository.follow(8L, 10L);
        Assertions.assertEquals(1, followed);

        List<User> followeesList = repository.getFollowees(10L);
        Assertions.assertTrue(followeesList.isEmpty());

        followeesList = repository.getFollowees(7L);
        Assertions.assertFalse(followeesList.isEmpty());
        Assertions.assertEquals(1, followeesList.size());
    }

}
