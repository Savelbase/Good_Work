package com.rm.toolkit.mediaStorage.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpiredTokenException extends ResponseStatusException {
    public ExpiredTokenException() {
        super(HttpStatus.UNAUTHORIZED, "А у вас токен просрочился");
    }
}
