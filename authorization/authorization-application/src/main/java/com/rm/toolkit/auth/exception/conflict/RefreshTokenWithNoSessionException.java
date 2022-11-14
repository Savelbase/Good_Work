package com.rm.toolkit.auth.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RefreshTokenWithNoSessionException extends ResponseStatusException {
    public RefreshTokenWithNoSessionException() {
        super(HttpStatus.CONFLICT, "С этим refresh токеном нет ассоциированных сессий");
    }
}
