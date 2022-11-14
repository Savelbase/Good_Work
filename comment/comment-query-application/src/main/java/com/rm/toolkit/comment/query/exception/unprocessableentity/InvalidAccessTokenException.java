package com.rm.toolkit.comment.query.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidAccessTokenException extends ResponseStatusException {

    public InvalidAccessTokenException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Это не access токен");
    }
}
