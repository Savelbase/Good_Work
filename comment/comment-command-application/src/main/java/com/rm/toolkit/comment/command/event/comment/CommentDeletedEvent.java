package com.rm.toolkit.comment.command.event.comment;


import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@Data
@DiscriminatorValue("COMMENT_DELETED")
@SuperBuilder
public class CommentDeletedEvent extends Event<CommentDeletedEvent.Payload> {

    public CommentDeletedEvent(){
        this.type = EventType.COMMENT_DELETED;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Payload extends EventPayload {
        private String id ;
        private ZonedDateTime dateTime;
        private boolean deleted ;
    }
}
