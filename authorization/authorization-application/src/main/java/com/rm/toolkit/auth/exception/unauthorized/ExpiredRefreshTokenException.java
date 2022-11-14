package com.rm.toolkit.auth.exception.unauthorized;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpiredRefreshTokenException extends ResponseStatusException {
    public ExpiredRefreshTokenException() {
        super(HttpStatus.UNAUTHORIZED, "А у вас токен просрочился");
    }
}
