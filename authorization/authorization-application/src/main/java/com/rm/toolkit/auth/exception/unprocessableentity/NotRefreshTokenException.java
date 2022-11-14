package com.rm.toolkit.auth.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotRefreshTokenException extends ResponseStatusException {
    public NotRefreshTokenException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Это не refresh токен");
    }
}
