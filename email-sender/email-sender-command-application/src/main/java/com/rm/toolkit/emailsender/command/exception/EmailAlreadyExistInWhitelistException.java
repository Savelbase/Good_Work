package com.rm.toolkit.emailsender.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistInWhitelistException extends ResponseStatusException {

    public EmailAlreadyExistInWhitelistException(String email) {
        super(HttpStatus.CONFLICT, "Email " + email + " уже занесен в whitelist");
    }
}