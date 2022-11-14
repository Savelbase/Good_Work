package com.rm.toolkit.oneToOne.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OneToOneForRMNotFoundException extends ResponseStatusException {

    public OneToOneForRMNotFoundException(String userId) {
        super(HttpStatus.NOT_FOUND, String.format("Для ресурсного менеджер с id: %s встреч не существует", userId));
    }
}