package com.rm.toolkit.user.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ActivityNotFoundException extends ResponseStatusException {
    public ActivityNotFoundException(String activityName, boolean isName) {
        super(HttpStatus.NOT_FOUND, String.format("Активность %s не найдена", activityName));
    }

    public ActivityNotFoundException(String activityId) {
        super(HttpStatus.NOT_FOUND, String.format("Активность с id %s не найдена", activityId));
    }
}
