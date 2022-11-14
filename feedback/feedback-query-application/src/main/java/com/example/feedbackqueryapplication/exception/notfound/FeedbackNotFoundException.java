package com.example.feedbackqueryapplication.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeedbackNotFoundException extends ResponseStatusException {

    public FeedbackNotFoundException(String feedbackId) {
        super(HttpStatus.NOT_FOUND, String.format("Фидбека с id %s не найден", feedbackId));
    }
}
