package com.rm.toolkit.oneToOne.query.event.oneToOne;

import com.rm.toolkit.oneToOne.query.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.rm.toolkit.oneToOne.query.event.EventPayload;
import com.rm.toolkit.oneToOne.query.event.EventType;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OneToOneUpdatedEvent extends Event<OneToOneUpdatedEvent.Payload> {

    public OneToOneUpdatedEvent() {
        this.type = EventType.ONE_TO_ONE_UPDATED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {

        protected String userId;

        protected String resourceManagerId;

        protected ZonedDateTime dateTime;

        protected String comment;

        protected boolean isOver;
    }
}