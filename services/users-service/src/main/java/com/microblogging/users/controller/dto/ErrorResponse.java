package com.microblogging.users.controller.dto;

public record ErrorResponse (int status, String type, String message) { }
