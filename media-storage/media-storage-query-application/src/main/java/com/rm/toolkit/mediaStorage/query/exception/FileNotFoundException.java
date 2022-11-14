package com.rm.toolkit.mediaStorage.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileNotFoundException extends ResponseStatusException {
    public FileNotFoundException(String url) {
        super(HttpStatus.NOT_FOUND, String.format("Файл по url %s не найден", url));
    }
}