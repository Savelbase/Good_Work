package com.rm.toolkit.user.command.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DepartmentAlreadyExistsException extends ResponseStatusException {

    public DepartmentAlreadyExistsException(String name) {
        super(HttpStatus.CONFLICT, String.format("Отдел с именем %s уже существует", name));
    }
}
