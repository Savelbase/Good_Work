package com.rm.toolkit.oneToOne.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserInPoolIsAbsentException extends ResponseStatusException {
    public UserInPoolIsAbsentException(String userId, String resourceManagerId) {
        super(HttpStatus.CONFLICT, String.format("Пользователь с id=%s отсутствует в пуле подчиненных пользователя с id=%s", userId, resourceManagerId));
    }
}
