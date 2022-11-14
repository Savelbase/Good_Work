package com.example.feedbackqueryapplication.exception.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAccessTokenException extends ResponseStatusException {

    public NotAccessTokenException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "В качестве токена передан не access токен");
    }
}
