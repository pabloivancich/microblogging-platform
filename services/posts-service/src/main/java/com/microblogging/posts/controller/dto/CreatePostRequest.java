package com.microblogging.posts.controller.dto;

public record CreatePostRequest(Long userId, String content) { }
