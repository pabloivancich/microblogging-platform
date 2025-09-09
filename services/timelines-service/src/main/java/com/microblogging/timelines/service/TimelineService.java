package com.microblogging.timelines.service;

import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.repository.TimelineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimelineService {

    private final TimelineRepository timelineRepository;

    public TimelineService(final TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public void addToTimeline(Long userId, Post post) {
        this.timelineRepository.addToTimeline(userId, post, post.createdAt().toEpochMilli());
    }

    public List<Post> getTimeline(Long userId) {
        return this.timelineRepository.getTimeline(userId);
    }

}
