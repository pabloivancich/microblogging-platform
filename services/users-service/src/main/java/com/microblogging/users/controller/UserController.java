package com.microblogging.users.controller;

import com.microblogging.users.controller.dto.CreateUserRequest;
import com.microblogging.users.model.User;
import com.microblogging.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a user from a username
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) throws Exception {
        userService.createUser(request.username());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieve a user by ID.
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUser(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        };
        return ResponseEntity.ok(userOptional.get());
    }

    /**
     * Retrieve a list with all users
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Delete user by user ID.
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

}
