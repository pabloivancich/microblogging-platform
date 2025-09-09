package com.microblogging.users.service;

import com.microblogging.users.exception.ValidationException;
import com.microblogging.users.model.User;
import com.microblogging.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String USERNAME_INVALID_LENGTH_EXCEPTION_MESSAGE = "Username length is not valid.";
    private static final String USERNAME_ALREADY_USED_EXCEPTION_MESSAGE = "Username is already used.";

    private final UserRepository userRepository;

    private static final int USERNAME_MAX_LENGTH = 50;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(String username) throws ValidationException {
        validateNewUsername(username);
        userRepository.createUser(username);
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Validates if username is valid or if it already used.
     * @param username to be validated
     */
    private void validateNewUsername(String username) throws ValidationException {
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new ValidationException(USERNAME_INVALID_LENGTH_EXCEPTION_MESSAGE);
        } else {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                throw new ValidationException(USERNAME_ALREADY_USED_EXCEPTION_MESSAGE);
            }
        }
    }

}
