package com.rm.toolkit.mediaStorage.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileNotProvidedException extends ResponseStatusException {
    public FileNotProvidedException(String url) {
        super(HttpStatus.CONFLICT, String.format("Неверное расширение файла %s", url));
    }
}
