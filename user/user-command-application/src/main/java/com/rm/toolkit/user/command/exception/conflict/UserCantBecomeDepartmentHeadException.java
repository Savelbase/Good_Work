package com.rm.toolkit.user.command.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserCantBecomeDepartmentHeadException extends ResponseStatusException {

    public UserCantBecomeDepartmentHeadException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
