package com.microblogging.posts.controller;

import com.microblogging.posts.controller.dto.CreatePostRequest;
import com.microblogging.posts.model.Post;
import com.microblogging.posts.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request) throws Exception {
        service.createPost(request.userId(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Post>> getPosts(@PathVariable Long userId) {
        List<Post> posts = service.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

}
