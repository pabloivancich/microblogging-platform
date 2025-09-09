package com.microblogging.posts.repository.impl;

import com.microblogging.posts.cassandra.CassandraInitializer;
import com.microblogging.posts.config.CassandraConfig;
import com.microblogging.posts.config.CassandraTestcontainersConfiguration;
import com.microblogging.posts.model.Post;
import com.microblogging.posts.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataCassandraTest
@Testcontainers
@Import({PostRepositoryImpl.class, CassandraTestcontainersConfiguration.class})
public class PostRepositoryImplTest {

    @Autowired
    private CqlTemplate cqlTemplate;

    @Autowired
    private PostRepository postRepository;

    @Test
    void testSaveAndFindById_Ok() {
        // Given a new post
        Post newPost = new Post(123L, "This is the content.");
        postRepository.save(newPost);

        // Then it can be retrieved by its ID
        List<Post> posts = postRepository.findByUserId(123L);

        assertEquals(1, posts.size(), "The saved post should be found in the database");
    }

    @Test
    void testFindAllByUserId_Ok() {
        Long userId = 456L;
        postRepository.save(new Post(userId, "Content A"));
        postRepository.save(new Post(userId, "Content B"));

        postRepository.save(new Post(789L, "Content C"));

        List<Post> userPosts = postRepository.findByUserId(userId);

        assertEquals(2, userPosts.size(), "Should return exactly two posts for the user");
        userPosts.forEach(post -> assertEquals(userId, post.getUserId(), "All returned posts should belong to the correct user"));
    }

}
