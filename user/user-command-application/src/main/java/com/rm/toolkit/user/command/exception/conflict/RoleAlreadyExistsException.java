package com.rm.toolkit.user.command.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleAlreadyExistsException extends ResponseStatusException {
    public RoleAlreadyExistsException(String name) {
        super(HttpStatus.CONFLICT, String.format("Роль с именем %s уже существует", name));
    }
}
