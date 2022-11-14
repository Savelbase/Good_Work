package com.rm.toolkit.comment.command.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
    public BadRequestException(String name) {
        super(HttpStatus.BAD_REQUEST, name);
    }
}
