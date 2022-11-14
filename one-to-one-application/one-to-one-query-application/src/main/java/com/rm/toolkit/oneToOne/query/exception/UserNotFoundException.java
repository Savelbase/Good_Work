package com.rm.toolkit.oneToOne.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(String userId) {
        super(HttpStatus.CONFLICT, String.format("Пользователя с id %s не существует", userId));
    }
}
