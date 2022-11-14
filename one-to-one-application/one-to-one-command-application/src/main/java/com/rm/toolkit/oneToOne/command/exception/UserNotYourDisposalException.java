package com.rm.toolkit.oneToOne.command.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotYourDisposalException extends ResponseStatusException {
    public UserNotYourDisposalException(String oneToOneId) {
        super(HttpStatus.FORBIDDEN, String.format("У вас нет прав чтоб завершить: " + oneToOneId));
    }
}
