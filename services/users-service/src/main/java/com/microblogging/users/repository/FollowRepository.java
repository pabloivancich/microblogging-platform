package com.microblogging.users.repository;

import com.microblogging.users.model.User;

import java.util.List;

public interface FollowRepository {

    int follow(Long followerId, Long followedId);

    int unfollow(Long followerId, Long followedId);

    List<User> getFollowers(Long userId);

    List<User> getFollowees(Long userId);

}
