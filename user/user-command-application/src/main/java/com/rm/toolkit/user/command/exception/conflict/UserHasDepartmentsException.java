package com.rm.toolkit.user.command.exception.conflict;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserHasDepartmentsException extends ResponseStatusException {
    public UserHasDepartmentsException(String email, boolean isEmail) {
        super(HttpStatus.CONFLICT, String.format("Нельзя провести операцию, так как пользователь с id %s явлеется " +
                "главой отделов", email));
    }

    public UserHasDepartmentsException(String userId) {
        super(HttpStatus.CONFLICT, String.format("Нельзя провести операцию, так как пользователь с id %s явлеется " +
                "главой отделов", userId));
    }
}
