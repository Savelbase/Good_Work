package com.rm.toolkit.feedbackcommandapplication.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OneToOneNotFoundException extends ResponseStatusException {

    public OneToOneNotFoundException(String oneToOneId) {
        super(HttpStatus.NOT_FOUND, String.format("121 встреча с id: %s не найдена", oneToOneId));
    }
}

