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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OneToOneCompletedEvent extends Event<OneToOneCompletedEvent.Payload> {

    public OneToOneCompletedEvent() {
        this.type = EventType.ONE_TO_ONE_COMPLETED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {

        protected boolean isOver;
    }
}