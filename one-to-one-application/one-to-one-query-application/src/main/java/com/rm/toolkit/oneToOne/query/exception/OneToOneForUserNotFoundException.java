package com.rm.toolkit.oneToOne.query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OneToOneForUserNotFoundException extends ResponseStatusException {

      public OneToOneForUserNotFoundException(String userId) {
        super(HttpStatus.NOT_FOUND, String.format("Для пользователя с id: %s встреч не существует", userId));
    }
}
