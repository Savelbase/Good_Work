package com.example.feedbackqueryapplication.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    FEEDBACK_CREATED(AssociatedObject.FEEDBACK),
    FEEDBACK_UPDATED(AssociatedObject.FEEDBACK),
    USER_CREATED(AssociatedObject.USER),
    USER_EDITED(AssociatedObject.USER);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        FEEDBACK, USER
    }
}
