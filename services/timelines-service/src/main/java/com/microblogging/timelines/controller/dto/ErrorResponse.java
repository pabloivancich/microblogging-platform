package com.microblogging.timelines.controller.dto;

public record ErrorResponse(int status, String type, String message) { }
