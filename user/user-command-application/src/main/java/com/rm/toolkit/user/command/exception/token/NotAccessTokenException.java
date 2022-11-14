package com.rm.toolkit.user.command.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAccessTokenException extends ResponseStatusException {
    public NotAccessTokenException() {
        super(HttpStatus.BAD_REQUEST, "Это не access токен");
    }
}
