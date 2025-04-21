package com.tcs.cuenta_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceError {
    private final HttpStatus status;
    private final String message;
    public ServiceError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}