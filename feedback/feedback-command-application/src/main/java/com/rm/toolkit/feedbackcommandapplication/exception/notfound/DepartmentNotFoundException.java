package com.rm.toolkit.feedbackcommandapplication.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DepartmentNotFoundException extends ResponseStatusException {

    public DepartmentNotFoundException(String departmentId) {
        super(HttpStatus.NOT_FOUND, String.format("Отдел с id %s не найден", departmentId));
    }
}
