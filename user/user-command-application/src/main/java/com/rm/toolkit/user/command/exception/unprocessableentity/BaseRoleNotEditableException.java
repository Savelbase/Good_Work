package com.rm.toolkit.user.command.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseRoleNotEditableException extends ResponseStatusException {
    public BaseRoleNotEditableException(String roleName) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Базовую роль с именем %s нельзя редактировать", roleName));
    }
}
