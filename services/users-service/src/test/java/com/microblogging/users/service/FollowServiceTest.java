package com.microblogging.users.service;

import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.kafka.FollowEventsPublisher;
import com.microblogging.users.model.User;
import com.microblogging.users.repository.impl.FollowRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private FollowRepositoryImpl followRepository;

    @Mock
    private FollowEventsPublisher followEventsPublisher;

    @Mock
    private UserService userService;

    @InjectMocks
    private FollowService service;

    @Test
    public void testFollow_Ok() throws Exception {
        User follower = new User(1L, "follower", Instant.now());
        User followee = new User(2L, "followee", Instant.now());
        when(userService.getUser(1L)).thenReturn(Optional.of(follower));
        when(userService.getUser(2L)).thenReturn(Optional.of(followee));

        when(followRepository.follow(anyLong(), anyLong())).thenReturn(1); // Follow done

        service.follow(1L, 2L);

        verify(followRepository, times(1)).follow(anyLong(), anyLong());
        verify(userService, times(2)).getUser(anyLong());
        verify(followEventsPublisher, times(1)).sendFollowEvent(anyLong(), anyLong());
    }

    @Test
    public void testFollow_SameUser() throws Exception {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.follow(1L, 1L)
        );

        assertEquals("You cannot follow yourself.", exception.getMessage());

        verify(followRepository, times(0)).follow(anyLong(), anyLong());
    }

    @Test
    public void testFollow_FollowerNotExist() {
        when(userService.getUser(400L)).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.follow(400L, 1L)
        );

        assertEquals("User does not exist.", exception.getMessage());

        verify(followRepository, times(0)).follow(anyLong(), anyLong());
        verify(userService, times(1)).getUser(anyLong());
    }

    @Test
    public void testFollow_FolloweeNotExist() {
        User follower = new User(1L, "follower", Instant.now());
        when(userService.getUser(1L)).thenReturn(Optional.of(follower));
        when(userService.getUser(400L)).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.follow(1L, 400L)
        );

        assertEquals("User does not exist.", exception.getMessage());

        verify(followRepository, times(0)).follow(anyLong(), anyLong());
        verify(userService, times(2)).getUser(anyLong());
    }

    @Test
    public void testUnfollow_Ok() {
        when(followRepository.unfollow(anyLong(), anyLong())).thenReturn(1);
        service.unfollow(1L, 2L);
        verify(followRepository, times(1)).unfollow(anyLong(), anyLong());
        verify(followEventsPublisher, times(1)).sendUnfollowEvent(anyLong(), anyLong());
    }

    @Test
    public void testGetFollowers_Ok() {
        User followerOne = new User(2L,"followerOne", Instant.now());
        User followerTwo = new User(3L,"followerTwo", Instant.now());
        when(followRepository.getFollowers(anyLong())).thenReturn(List.of(followerOne, followerTwo));
        List<User> followers = service.getFollowers(1L);

        Assertions.assertFalse(followers.isEmpty());
        Assertions.assertEquals(2, followers.size());
        verify(followRepository, times(1)).getFollowers(1L);
    }

    @Test
    public void testGetFollowers_EmptyList() {
        when(followRepository.getFollowers(anyLong())).thenReturn(List.of());
        List<User> followers = service.getFollowers(1L);

        Assertions.assertTrue(followers.isEmpty());
        verify(followRepository, times(1)).getFollowers(1L);
    }

    @Test
    public void testGetFollowees_Ok() {
        User followedOne = new User(2L,"followerOne", Instant.now());
        User followedTwo = new User(3L,"followerTwo", Instant.now());
        when(followRepository.getFollowees(anyLong())).thenReturn(List.of(followedOne, followedTwo));
        List<User> followees = service.getFollowees(1L);

        Assertions.assertFalse(followees.isEmpty());
        Assertions.assertEquals(2, followees.size());
        verify(followRepository, times(1)).getFollowees(1L);
    }

    @Test
    public void testGetFollowees_EmptyList() {
        when(followRepository.getFollowees(anyLong())).thenReturn(List.of());
        List<User> followees = service.getFollowees(1L);

        Assertions.assertTrue(followees.isEmpty());
        verify(followRepository, times(1)).getFollowees(1L);
    }

}
