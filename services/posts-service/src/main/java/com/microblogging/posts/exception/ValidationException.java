package com.microblogging.posts.exception;

/**
 * Posts service validation exception
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}