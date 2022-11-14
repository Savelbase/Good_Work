package com.rm.toolkit.user.command.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AdminRoleIsNotEditableException extends ResponseStatusException {
    public AdminRoleIsNotEditableException(String roleName) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Админскую роль с именем %s нельзя редактировать",
                roleName));
    }
}
