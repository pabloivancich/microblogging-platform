package com.microblogging.users.service;

import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.kafka.FollowEventsPublisher;
import com.microblogging.users.model.User;
import com.microblogging.users.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowEventsPublisher followEventsPublisher;

    private final FollowRepository followRepository;

    private final UserService userService;

    private static final String FOLLOW_YOURSELF_MESSAGE = "You cannot follow yourself.";
    private static final String USER_DOES_NOT_EXIST = "User does not exist.";

    @Autowired
    public FollowService(
            final FollowEventsPublisher followEventsProducerService,
            final FollowRepository followRepository,
            final UserService userService
    ) {
        this.followEventsPublisher = followEventsProducerService;
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Transactional
    public void follow(Long followerId, Long followeeId) throws ValidationException {
        validateUsers(followerId, followeeId);
        followRepository.follow(followerId, followeeId);

        followEventsPublisher.sendFollowEvent(followerId, followeeId);
    }

    @Transactional
    public void unfollow(Long followerId, Long followeeId) {
        followRepository.unfollow(followerId, followeeId);

        followEventsPublisher.sendUnfollowEvent(followerId, followeeId);
    }

    public List<User> getFollowers(Long userId) {
        return followRepository.getFollowers(userId);
    }

    public List<User> getFollowees(Long userId) {
        return followRepository.getFollowees(userId);
    }

    private void validateUsers(final Long followerId, final Long followeeId) throws ValidationException {
        if (followerId.equals(followeeId)) {
            throw new ValidationException(FOLLOW_YOURSELF_MESSAGE);
        } else {
            Optional<User> followerOptional = userService.getUser(followerId);
            if (followerOptional.isEmpty()) {
                throw new ValidationException(USER_DOES_NOT_EXIST);
            }

            Optional<User> followeeOptional = userService.getUser(followeeId);
            if (followeeOptional.isEmpty()) {
                throw new ValidationException(USER_DOES_NOT_EXIST);
            }
        }
    }

}
