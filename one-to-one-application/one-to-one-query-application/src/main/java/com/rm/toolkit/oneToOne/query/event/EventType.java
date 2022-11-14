package com.rm.toolkit.oneToOne.query.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    ONE_TO_ONE_CREATED(AssociatedObject.ONE_TO_ONE),
    ONE_TO_ONE_UPDATED(AssociatedObject.ONE_TO_ONE),
    ONE_TO_ONE_DELETED(AssociatedObject.ONE_TO_ONE),
    ONE_TO_ONE_COMPLETED(AssociatedObject.ONE_TO_ONE),
    CHANGE_ONE_TO_ONE_INTERVAL(AssociatedObject.ONE_TO_ONE),
    USER_CREATED(AssociatedObject.USER),
    USER_EDITED(AssociatedObject.USER);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        ONE_TO_ONE, USER
    }
}
