package com.rm.toolkit.mediaStorage.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileAlreadyExistException extends ResponseStatusException {
    public FileAlreadyExistException(String url) {
        super(HttpStatus.CONFLICT, String.format("Файл с именем %s уже существует", url));
    }
}