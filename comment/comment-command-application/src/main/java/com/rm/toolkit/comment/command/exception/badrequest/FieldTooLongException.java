package com.rm.toolkit.comment.command.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FieldTooLongException extends ResponseStatusException {
    public FieldTooLongException(String name) {
        super(HttpStatus.BAD_REQUEST, "Field too long: " + name);
    }
}
