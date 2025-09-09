package com.microblogging.timelines.controller;

import com.microblogging.timelines.model.Post;
import com.microblogging.timelines.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timelines/")
public class TimelineController {

    private TimelineService timelineService;

    @Autowired
    public TimelineController(final TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Post>> getTimeline(@PathVariable Long userId) {
        List<Post> posts = this.timelineService.getTimeline(userId);
        return ResponseEntity.ok(posts);
    }

}
