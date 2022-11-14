package com.rm.toolkit.mediaStorage.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    AVATAR_UPLOADED(AssociatedObject.MEDIA_FILE),
    AVATAR_CONFIRMED(AssociatedObject.MEDIA_FILE),
    AVATAR_DELETED(AssociatedObject.MEDIA_FILE),
    USER_CREATED(AssociatedObject.USER),
    USER_EDITED(AssociatedObject.USER),
    USER_STATUS_CHANGED(AssociatedObject.USER),
    USER_BLOCKED(AssociatedObject.USER),
    USER_DEPARTMENT_CHANGED(AssociatedObject.USER),
    USER_RM_CHANGED(AssociatedObject.USER),
    USER_ROLE_CHANGED(AssociatedObject.USER);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        MEDIA_FILE,
        USER
    }
}
