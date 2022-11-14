package com.rm.toolkit.user.command.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistException extends ResponseStatusException {
    public UserAlreadyExistException(String email, boolean isEmail) {
        super(HttpStatus.CONFLICT, String.format("Пользователь с email %s уже существует", email));
    }

    public UserAlreadyExistException(String userId) {
        super(HttpStatus.CONFLICT, String.format("Пользователь с id %s уже существует", userId));
    }
}
