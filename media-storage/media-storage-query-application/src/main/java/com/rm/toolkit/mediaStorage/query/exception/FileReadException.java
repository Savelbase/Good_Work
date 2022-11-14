package com.rm.toolkit.mediaStorage.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileReadException extends ResponseStatusException {
    public FileReadException(String id) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, String.format("File %s не обработан", id));
    }
}
