package com.microblogging.posts.service;

import com.microblogging.posts.exception.ValidationException;
import com.microblogging.posts.kafka.PostEventsPublisher;
import com.microblogging.posts.model.Post;
import com.microblogging.posts.repository.impl.PostRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepositoryImpl postRepository;

    @Mock
    private PostEventsPublisher postEventsPublisher;

    @InjectMocks
    private PostService postService;

    @Test
    public void testCreatePost_Ok() throws ValidationException {
        Long userId = 1L;
        String content = "Test content";

        doNothing().when(postRepository).save(any(Post.class));
        doNothing().when(postEventsPublisher).sendPostCreatedEvent(any(Post.class));

        postService.createPost(userId, content);

        verify(postRepository, times(1)).save(any(Post.class));
        verify(postEventsPublisher, times(1)).sendPostCreatedEvent(any(Post.class));
    }

    @Test
    public void testCreatePost_InvalidContent() {
        Long userId = 1L;
        String invalidContent = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaa"; // 501 'a' characters

        ValidationException exception = assertThrows(ValidationException.class,
                () -> postService.createPost(userId, invalidContent)
        );

        Assertions.assertEquals("Context length is greater than maximum.", exception.getMessage());

        verify(postRepository, times(0)).save(any(Post.class));
        verify(postEventsPublisher, times(0)).sendPostCreatedEvent(any(Post.class));
    }

    @Test
    public void testGetPostsByUserId_Ok() {
        Post postOne = new Post(1L, "content one");
        Post postTwo = new Post(1L, "content two");
        when(postRepository.findByUserId(anyLong())).thenReturn(List.of(postOne, postTwo));

        List<Post> posts = postService.getPostsByUserId(1L);

        Assertions.assertFalse(posts.isEmpty());
        Assertions.assertEquals(2, posts.size());
    }

    @Test
    public void testGetPostsByUserId_Empty() {
        when(postRepository.findByUserId(anyLong())).thenReturn(List.of());

        List<Post> posts = postService.getPostsByUserId(1L);

        Assertions.assertTrue(posts.isEmpty());

    }

}
