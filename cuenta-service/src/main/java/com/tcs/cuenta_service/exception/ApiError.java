package com.tcs.cuenta_service.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiError {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final List<String> messages;
    private final String path;
    public ApiError(int status, String error, List<String> messages, String path) {
        this.status = status;
        this.error = error;
        this.messages = messages;
        this.path = path;
    }
}