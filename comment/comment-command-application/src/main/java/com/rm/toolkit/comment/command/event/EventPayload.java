package com.rm.toolkit.comment.command.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.comment.command.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.command.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.command.event.comment.CommentEditedEvent;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommentCreatedEvent.Payload.class, name = "COMMENT_CREATED"),
        @JsonSubTypes.Type(value = CommentEditedEvent.Payload.class, name = "COMMENT_UPDATED"),
        @JsonSubTypes.Type(value = CommentDeletedEvent.Payload.class, name = "COMMENT_DELETED")
})
@Data
@NoArgsConstructor
public class EventPayload {
    protected EventType type ;
}
