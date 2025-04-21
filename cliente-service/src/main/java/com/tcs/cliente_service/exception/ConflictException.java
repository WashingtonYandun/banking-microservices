package com.tcs.cliente_service.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String msg) { super(msg); }
}