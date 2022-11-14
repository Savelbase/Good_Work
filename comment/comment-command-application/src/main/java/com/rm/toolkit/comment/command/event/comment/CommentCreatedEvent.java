package com.rm.toolkit.comment.command.event.comment;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("COMMENT_CREATED")
@SuperBuilder
public class CommentCreatedEvent extends Event<CommentCreatedEvent.Payload> {

    public CommentCreatedEvent(){
        this.type = EventType.COMMENT_CREATED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        private String id ;
        private ZonedDateTime dateTime;
        private String userID ;
        private String senderId ;
        private String text ;
        private boolean deleted ;
    }

}
