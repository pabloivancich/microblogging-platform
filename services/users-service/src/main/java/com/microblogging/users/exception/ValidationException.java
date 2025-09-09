package com.microblogging.users.exception;

/**
 * Users service validation exception
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
