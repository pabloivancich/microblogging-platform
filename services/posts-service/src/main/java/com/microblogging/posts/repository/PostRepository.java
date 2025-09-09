package com.microblogging.posts.repository;

import com.microblogging.posts.model.Post;

import java.util.List;

/**
 * Interface defining the contract for our Post data access layer.
 */
public interface PostRepository {

    List<Post> findByUserId(Long userId);

    void save(Post post);
}
