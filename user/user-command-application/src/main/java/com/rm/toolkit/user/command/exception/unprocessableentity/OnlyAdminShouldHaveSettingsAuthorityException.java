package com.rm.toolkit.user.command.exception.unprocessableentity;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OnlyAdminShouldHaveSettingsAuthorityException extends ResponseStatusException {
    public OnlyAdminShouldHaveSettingsAuthorityException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Правами EDIT_ROLES, EDIT_DEPARTMENTS и EDIT_INTERVALS может обладать" +
                " только администратор");
    }

}
