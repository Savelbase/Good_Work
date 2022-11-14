package com.rm.toolkit.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    USER_MAX_LOGIN_ATTEMPTS_REACHED(AssociatedObject.USER),
    USER_CREATED(AssociatedObject.USER),
    USER_EDITED(AssociatedObject.USER),
    USER_STATUS_CHANGED(AssociatedObject.USER),
    USER_BLOCKED(AssociatedObject.USER),
    USER_ROLE_CHANGED(AssociatedObject.USER),
    USER_DEPARTMENT_CHANGED(AssociatedObject.USER),
    ROLE_CREATED(AssociatedObject.ROLE),
    ROLE_EDITED(AssociatedObject.ROLE),
    ROLE_DELETED(AssociatedObject.ROLE);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        USER,
        ROLE
    }
}
