package com.rm.toolkit.user.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    USER_MAX_LOGIN_ATTEMPTS_REACHED(AssociatedObject.USER),
    USER_CREATED(AssociatedObject.USER),
    USER_EDITED(AssociatedObject.USER),
    USER_STATUS_CHANGED(AssociatedObject.USER),
    USER_BLOCKED(AssociatedObject.USER),
    USER_DEPARTMENT_CHANGED(AssociatedObject.USER),
    USER_RM_CHANGED(AssociatedObject.USER),
    USER_ROLE_CHANGED(AssociatedObject.USER),
    CHANGE_USERS_DEPARTMENT(AssociatedObject.USER),
    ROLE_CREATED(AssociatedObject.ROLE),
    ROLE_EDITED(AssociatedObject.ROLE),
    ROLE_DELETED(AssociatedObject.ROLE),
    DEPARTMENT_CREATED(AssociatedObject.DEPARTMENT),
    DEPARTMENT_EDITED(AssociatedObject.DEPARTMENT),
    DEPARTMENT_DELETED(AssociatedObject.DEPARTMENT);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        USER,
        ROLE,
        DEPARTMENT
    }
}
