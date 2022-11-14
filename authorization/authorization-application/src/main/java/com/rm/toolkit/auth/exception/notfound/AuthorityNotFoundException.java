package com.rm.toolkit.auth.exception.notfound;

import com.rm.toolkit.auth.security.AuthorityType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthorityNotFoundException extends ResponseStatusException {
    public AuthorityNotFoundException(AuthorityType activityName) {
        super(HttpStatus.NOT_FOUND, String.format("Право %s не найдена", activityName));
    }

    public AuthorityNotFoundException(String activityId) {
        super(HttpStatus.NOT_FOUND, String.format("Право с id %s не найдена", activityId));
    }
}
