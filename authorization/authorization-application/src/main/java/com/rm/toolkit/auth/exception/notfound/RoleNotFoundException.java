package com.rm.toolkit.auth.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class RoleNotFoundException extends ResponseStatusException {
    public RoleNotFoundException(String roleName) {
        super(HttpStatus.NOT_FOUND, String.format("Роли с именем %s не существует", roleName));
    }

    public RoleNotFoundException(UUID roleId) {
        super(HttpStatus.NOT_FOUND, String.format("Роли с id %s не существует", roleId));
    }
}
