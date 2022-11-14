package com.rm.toolkit.feedbackcommandapplication.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeedbackNotFoundException extends ResponseStatusException {

    public FeedbackNotFoundException(String feedbackId) {
        super(HttpStatus.NOT_FOUND, String.format("Фидбек с id %s не найден", feedbackId));
    }
}
