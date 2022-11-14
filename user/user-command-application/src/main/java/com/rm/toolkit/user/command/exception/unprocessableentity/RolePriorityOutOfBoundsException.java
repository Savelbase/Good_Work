package com.rm.toolkit.user.command.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RolePriorityOutOfBoundsException extends ResponseStatusException {
    public RolePriorityOutOfBoundsException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Приоритет должен быть в диапазоне 1<x<ваш приоритет");
    }
}
