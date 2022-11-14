package com.rm.toolkit.user.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(String email, boolean isEmail) {
        super(HttpStatus.NOT_FOUND, String.format("Пользователя с email %s не существует", email));
    }

    public UserNotFoundException(String userId) {
        super(HttpStatus.NOT_FOUND, String.format("Пользователя с id %s не существует", userId));
    }
}
