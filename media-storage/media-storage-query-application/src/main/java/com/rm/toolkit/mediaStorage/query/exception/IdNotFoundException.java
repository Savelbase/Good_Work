package com.rm.toolkit.mediaStorage.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IdNotFoundException extends ResponseStatusException {
    public IdNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, String.format("Id %s не найден", id));
    }
}
