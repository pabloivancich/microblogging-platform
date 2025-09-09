package com.microblogging.users.controller;

import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.model.User;
import com.microblogging.users.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(final FollowService followService) {
        this.followService = followService;
    }

    /**
     * Creates a follow relationship between a follower and a followee.
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/{followerId}/follow/{followedId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> follow(@PathVariable Long followerId, @PathVariable Long followedId) throws ValidationException {
        followService.follow(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a follow relationship between a follower and a followee.
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{followerId}/follow/{followedId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> unfollow(@PathVariable Long followerId, @PathVariable Long followedId) {
        followService.unfollow(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all followers for a given user.
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}/followers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    /**
     * Retrieves all followees for a given user.
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}/followees",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<User>> getFollowees(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowees(userId));
    }

}
