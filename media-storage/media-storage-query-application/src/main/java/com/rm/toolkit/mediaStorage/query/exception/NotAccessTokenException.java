package com.rm.toolkit.mediaStorage.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAccessTokenException extends ResponseStatusException {
    public NotAccessTokenException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Это не access токен");
    }
}
