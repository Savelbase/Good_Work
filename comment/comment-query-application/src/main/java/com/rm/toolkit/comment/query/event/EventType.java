package com.rm.toolkit.comment.query.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EventType {
    COMMENT_CREATED(AssociatedObject.COMMENT),
    COMMENT_UPDATED(AssociatedObject.COMMENT),
    COMMENT_DELETED(AssociatedObject.COMMENT);

    @Getter
    private final AssociatedObject associatedObject;

    public enum AssociatedObject {
        COMMENT
    }
}
