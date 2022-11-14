package com.rm.toolkit.auth.exception.unauthorized;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordIncorrectException extends ResponseStatusException {
    private final Integer loginAttempts;

    public PasswordIncorrectException(Integer loginAttempts) {
        // Todo Договориться с фронтом о 401
//        super(HttpStatus.UNAUTHORIZED, "Неправильный пароль");
        super(HttpStatus.BAD_REQUEST, "Неправильный пароль");
        this.loginAttempts = loginAttempts;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }
}
