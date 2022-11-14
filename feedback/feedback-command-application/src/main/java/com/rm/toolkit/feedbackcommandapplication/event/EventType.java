package com.rm.toolkit.feedbackcommandapplication.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    FEEDBACK_CREATED(AssociatedObject.FEEDBACK),
    FEEDBACK_UPDATED(AssociatedObject.FEEDBACK),
    USER_CREATED(AssociatedObject.USER),
    USER_EDITED(AssociatedObject.USER),
    CHANGE_USERS_DEPARTMENT(AssociatedObject.USER),
    USER_DEPARTMENT_CHANGED(AssociatedObject.USER),
    DEPARTMENT_CREATED(AssociatedObject.DEPARTMENT),
    DEPARTMENT_EDITED(AssociatedObject.DEPARTMENT),
    DEPARTMENT_DELETED(AssociatedObject.DEPARTMENT),
    ONE_TO_ONE_CREATED(AssociatedObject.ONE_TO_ONE),
    ONE_TO_ONE_UPDATED(AssociatedObject.ONE_TO_ONE),
    ONE_TO_ONE_COMPLETED(AssociatedObject.ONE_TO_ONE),
    ONE_TO_ONE_DELETED(AssociatedObject.ONE_TO_ONE);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        FEEDBACK, USER, DEPARTMENT, ONE_TO_ONE
    }
}
