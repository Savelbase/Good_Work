package com.rm.toolkit.user.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DepartmentNotFoundException extends ResponseStatusException {
    public DepartmentNotFoundException(String departmentName, boolean isName) {
        super(HttpStatus.NOT_FOUND, String.format("Отдела с именем %s не существует", departmentName));
    }

    public DepartmentNotFoundException(String departmentId) {
        super(HttpStatus.NOT_FOUND, String.format("Отдела с id %s не существует", departmentId));
    }
}
