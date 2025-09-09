package com.microblogging.users.repository.impl;

import com.microblogging.users.model.User;
import com.microblogging.users.repository.UserRepository;
import com.microblogging.users.repository.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final UserRowMapper userRowMapper;

    @Autowired
    public UserRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = new UserRowMapper();
    }

    /**
     * Creates a user from a username.
     */
    @Override
    public void createUser(String username) {
        jdbcTemplate.update(
        "INSERT INTO users (username) VALUES (?)",
                username
        );
    }

    /**
     * Finds user by ID.
     */
    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query(
            "SELECT id, username, created_at FROM users WHERE id = ?",
                userRowMapper,
                id
        ).stream().findFirst();
    }

    /**
     * Finds user by USERNAME.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(
            "SELECT id, username, created_at FROM users WHERE username = ?",
                userRowMapper,
                username
        ).stream().findFirst();
    }

    /**
     * Finds all users.
     */
    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT id, username, created_at FROM users",
                userRowMapper
        );
    }

    /**
     * Deletes a user by ID.
     */
    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(
            "DELETE FROM users WHERE id = ?",
                id
        );
    }

}



