package com.microblogging.posts.controller.dto;

public record ErrorResponse(int status, String type, String message) { }
