package com.microblogging.users.repository;

import com.microblogging.users.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void createUser(String username);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    int deleteById(Long id);

}
