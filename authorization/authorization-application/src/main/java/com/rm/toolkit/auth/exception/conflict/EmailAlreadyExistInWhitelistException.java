package com.rm.toolkit.auth.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistInWhitelistException extends ResponseStatusException {

    public EmailAlreadyExistInWhitelistException(String email) {
        super(HttpStatus.CONFLICT, "Пользователя с email " + email + " уже занесен в whitelist");
    }
}
