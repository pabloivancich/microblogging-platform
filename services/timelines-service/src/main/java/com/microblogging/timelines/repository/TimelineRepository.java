package com.microblogging.timelines.repository;

import com.microblogging.timelines.model.Post;

import java.util.List;

public interface TimelineRepository {

    void addToTimeline(Long userId, Post post, long timestamp);

    List<Post> getTimeline(Long userId);

    boolean timelineExists(Long userId);

}
