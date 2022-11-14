package com.rm.toolkit.user.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleNotFoundException extends ResponseStatusException {
    public RoleNotFoundException(String roleName, boolean isName) {
        super(HttpStatus.NOT_FOUND, String.format("Роли с именем %s не существует", roleName));
    }

    public RoleNotFoundException(String roleId) {
        super(HttpStatus.NOT_FOUND, String.format("Роли с id %s не существует", roleId));
    }
}
