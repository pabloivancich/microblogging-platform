package com.microblogging.users.repository.impl;

import com.microblogging.users.config.PostgreSQLTestcontainersConfiguration;
import com.microblogging.users.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@JdbcTest
@Import({UserRepositoryImpl.class, PostgreSQLTestcontainersConfiguration.class})
public class UserRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepositoryImpl repository;

    @Test
    public void testCreateUserOk() {
        String username = "createdUser";
        repository.createUser(username);
        Optional<User> userOptional = repository.findByUsername(username);

        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(username, userOptional.get().getUsername());
    }

    @Test
    public void testFindByIdOK() {
        Long id = 1L;

        Optional<User> userOptional = repository.findById(id);
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(id, userOptional.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 4000L;

        Optional<User> userOptional = repository.findById(id);
        Assertions.assertTrue(userOptional.isEmpty());
    }

    @Test
    public void testFindByUsernameOK() {
        String username = "user01";
        Optional<User> userOptional = repository.findByUsername(username);

        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(username, userOptional.get().getUsername());
    }

    @Test
    public void testFindByUsernameNotFound() {
        String username = "unknownUser";
        Optional<User> userOptional = repository.findByUsername(username);
        Assertions.assertTrue(userOptional.isEmpty());
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = repository.findAll();
        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    public void testDeleteUserOk() {
        int deleted = repository.deleteById(1L);
        Assertions.assertEquals(1, deleted);
    }

    @Test
    public void testDeleteUserNotExist() {
        int deleted = repository.deleteById(4000L);
        Assertions.assertEquals(0, deleted);
    }

}
