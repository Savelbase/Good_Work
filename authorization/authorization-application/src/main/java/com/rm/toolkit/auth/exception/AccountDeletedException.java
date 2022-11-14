package com.rm.toolkit.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountDeletedException extends ResponseStatusException {
    public AccountDeletedException() {
        super(HttpStatus.UNAUTHORIZED, "Данный пользователь удалён");
    }
}
