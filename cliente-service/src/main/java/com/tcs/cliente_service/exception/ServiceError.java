package com.tcs.cliente_service.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class ServiceError {
    private final HttpStatus status;
    private final String message;
    public ServiceError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}