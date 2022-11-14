package com.rm.toolkit.mediaStorage.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UrlNotFoundException extends ResponseStatusException {
    public UrlNotFoundException(String url) {
        super(HttpStatus.NOT_FOUND, String.format("Url %s не найден", url));
    }
}
