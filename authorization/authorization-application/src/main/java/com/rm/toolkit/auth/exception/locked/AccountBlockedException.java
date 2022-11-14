package com.rm.toolkit.auth.exception.locked;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountBlockedException extends ResponseStatusException {
    public AccountBlockedException() {
        super(HttpStatus.LOCKED, "Ваша учётная запись заблокирована. " +
                "Для восстановления доступа обратитесь к своему ресурсному менеджеру.");
    }
}
