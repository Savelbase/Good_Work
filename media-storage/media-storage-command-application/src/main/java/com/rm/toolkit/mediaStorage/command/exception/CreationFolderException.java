package com.rm.toolkit.mediaStorage.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CreationFolderException extends ResponseStatusException {
    public CreationFolderException(String path) {
        super(HttpStatus.CONFLICT, String.format("Не удалось создать папку %s", path));
    }
}
