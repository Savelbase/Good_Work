package com.rm.toolkit.user.command.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseRoleNotDeletableException extends ResponseStatusException {
    public BaseRoleNotDeletableException(String roleName) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Базовую роль с именем %s нельзя удалить", roleName));
    }
}
