package com.rm.toolkit.comment.query.event.comment;

import com.rm.toolkit.comment.query.event.Event;
import com.rm.toolkit.comment.query.event.EventPayload;
import com.rm.toolkit.comment.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CommentEditedEvent extends Event<CommentEditedEvent.Payload> {

    public CommentEditedEvent() {
        this.type = EventType.COMMENT_UPDATED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {
        private String id;
        private ZonedDateTime dateTime;
        private String userID;
        private String senderId;
        private String text;
        private boolean deleted;
    }
}
