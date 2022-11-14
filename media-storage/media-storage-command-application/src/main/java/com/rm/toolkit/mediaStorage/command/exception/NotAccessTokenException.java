package com.rm.toolkit.mediaStorage.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAccessTokenException extends ResponseStatusException {
    public NotAccessTokenException() {
        super(HttpStatus.BAD_REQUEST, "Это не access токен");
    }
}
