package com.rm.toolkit.oneToOne.query.exception;

import org.springframework.security.access.AccessDeniedException;

public class NoAuthorityException extends AccessDeniedException {
    public NoAuthorityException(String msg) {
        super(msg);
    }
}
