package com.rm.toolkit.user.query.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleNotFoundException extends ResponseStatusException {

    public RoleNotFoundException(String roleId) {
        super(HttpStatus.NOT_FOUND, String.format("Роли с id %s не существует", roleId));
    }
}
