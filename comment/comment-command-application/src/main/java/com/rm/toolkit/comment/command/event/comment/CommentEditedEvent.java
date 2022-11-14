package com.rm.toolkit.comment.command.event.comment;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Data
@Entity
@DiscriminatorValue("COMMENT_UPDATED")
@SuperBuilder
public class CommentEditedEvent extends Event<CommentEditedEvent.Payload> {

    public CommentEditedEvent(){
        this.type = EventType.COMMENT_UPDATED;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload extends EventPayload{
        private String id ;
        private ZonedDateTime dateTime;
        private String userID ;
        private String senderId ;
        private String text ;
        private boolean deleted ;
    }
}
