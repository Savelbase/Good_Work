package com.rm.toolkit.comment.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.comment.query.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentEditedEvent;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommentCreatedEvent.Payload.class, name = "COMMENT_CREATED"),
        @JsonSubTypes.Type(value = CommentEditedEvent.Payload.class, name = "COMMENT_UPDATED"),
        @JsonSubTypes.Type(value = CommentDeletedEvent.Payload.class, name = "COMMENT_DELETED")
})
@NoArgsConstructor
public class EventPayload {
}
