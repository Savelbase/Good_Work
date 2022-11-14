package com.rm.toolkit.oneToOne.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    ONE_TO_ONE_CREATED(AssociatedObject.ONE_TO_ONE,1),
    ONE_TO_ONE_UPDATED(AssociatedObject.ONE_TO_ONE,1),
    ONE_TO_ONE_DELETED(AssociatedObject.ONE_TO_ONE,1),
    ONE_TO_ONE_COMPLETED(AssociatedObject.ONE_TO_ONE,1),
    CHANGE_ONE_TO_ONE_INTERVAL(AssociatedObject.ONE_TO_ONE,1),
    USER_CREATED(AssociatedObject.USER,1),
    USER_EDITED(AssociatedObject.USER,1);

    @Getter
    private final AssociatedObject associatedObject;

    @Getter
    private final int version;

    public enum AssociatedObject {
        ONE_TO_ONE,USER
    }

}
