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
public class OneToOneDeletedEvent extends Event<OneToOneDeletedEvent.Payload> {

    public OneToOneDeletedEvent() {
        this.type = EventType.ONE_TO_ONE_DELETED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {

        protected boolean isDeleted;
    }
}