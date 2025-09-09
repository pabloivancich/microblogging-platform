package com.microblogging.timelines.kafka.event;

/**
 * Represents a follow/unfollow event.
 * @param followerId The ID of the user who initiated the action.
 * @param followeeId The ID of the user who is the target of the action.
 */
public record FollowEvent(Long followerId, Long followeeId) { }