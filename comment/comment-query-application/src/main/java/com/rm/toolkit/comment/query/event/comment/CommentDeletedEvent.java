package com.rm.toolkit.comment.query.event.comment;

import com.rm.toolkit.comment.query.event.Event;
import com.rm.toolkit.comment.query.event.EventPayload;
import com.rm.toolkit.comment.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CommentDeletedEvent extends Event<CommentDeletedEvent.Payload> {

    public CommentDeletedEvent() {
        this.type = EventType.COMMENT_DELETED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {
        private String id;
        private ZonedDateTime dateTime;
        private boolean deleted;
    }
}
