package com.rm.toolkit.feedbackcommandapplication.event.oneToOne;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OneToOneCreatedEvent extends Event<OneToOneCreatedEvent.Payload> {

    public OneToOneCreatedEvent() {
        this.type = EventType.ONE_TO_ONE_CREATED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {
        protected String userId;

        protected String resourceManagerId;

        protected ZonedDateTime dateTime;

        protected String comment;

        protected boolean isOver;

        protected boolean isDeleted;
    }
}