package com.microblogging.posts.service;

import com.microblogging.posts.exception.ValidationException;
import com.microblogging.posts.kafka.PostEventsPublisher;
import com.microblogging.posts.model.Post;
import com.microblogging.posts.repository.impl.PostRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepositoryImpl postRepository;

    private final PostEventsPublisher postEventsPublisher;

    private static final int POST_CONTENT_MAX_LENGTH = 500;

    @Autowired
    public PostService(final PostRepositoryImpl postRepository, final PostEventsPublisher postEventsPublisher) {
        this.postRepository = postRepository;
        this.postEventsPublisher = postEventsPublisher;
    }

    public void createPost(Long userId, String content) throws ValidationException {
        validatePostContent(content);
        Post post = new Post(userId, content);

        postRepository.save(post);
        postEventsPublisher.sendPostCreatedEvent(post);
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    private void validatePostContent(String content) throws ValidationException {
        if (content.length() > POST_CONTENT_MAX_LENGTH) {
            throw new ValidationException("Context length is greater than maximum.");
        }
    }

}
