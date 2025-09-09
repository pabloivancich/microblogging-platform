package com.microblogging.users.service;

import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.model.User;
import com.microblogging.users.repository.impl.UserRepositoryImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser_Ok() throws Exception {
        doNothing().when(userRepository).createUser(anyString());
        userService.createUser("testUser");
        verify(userRepository, times(1)).createUser(anyString());
    }

    @Test
    public void testCreateUser_UsernameInvalid() throws Exception {
        String longUsername = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 51 'a' characters username
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(longUsername))
        ;

        assertEquals("Username length is not valid.", exception.getMessage());

        verify(userRepository, times(0)).createUser(anyString());
    }

    @Test
    public void testCreateUser_UsernameUsed() throws Exception {
        String usedUsername = "firstUser";
        User user = new User(1L,usedUsername, Instant.now());
        when(userRepository.findByUsername(usedUsername)).thenReturn(Optional.of(user));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(usedUsername)
        );

        assertEquals("Username is already used.", exception.getMessage());

        verify(userRepository, times(0)).createUser(anyString());
    }

    @Test
    public void testGetUser_Ok() {
        User testUser = new User(1L, "testUser", Instant.now());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        Optional<User> userOptional = userService.getUser(1L);

        verify(userRepository, times(1)).findById(anyLong());
        Assertions.assertTrue(userOptional.isPresent());
        assertEquals("testUser", userOptional.get().getUsername());
    }

    @Test
    public void testGetUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.getUser(1L);

        verify(userRepository, times(1)).findById(anyLong());
        Assertions.assertTrue(userOptional.isEmpty());
    }

    @Test
    public void testFindAll_Ok() {
        User testUser = new User(1L, "testUser", Instant.now());
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getUsers();

        verify(userRepository, times(1)).findAll();
        Assertions.assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    public void testFindAll_Empty() {
        when(userRepository.findAll()).thenReturn(List.of());
        List<User> users = userService.getUsers();

        verify(userRepository, times(1)).findAll();
        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    public void testDeleteUser_Ok() {
        when(userRepository.deleteById(anyLong())).thenReturn(1);
        userService.removeUser(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

}
