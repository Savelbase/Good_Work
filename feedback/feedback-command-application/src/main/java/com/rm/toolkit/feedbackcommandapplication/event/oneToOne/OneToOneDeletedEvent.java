package com.rm.toolkit.feedbackcommandapplication.event.oneToOne;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OneToOneDeletedEvent extends Event<OneToOneDeletedEvent.Payload> {

    public OneToOneDeletedEvent() {
        this.type = EventType.ONE_TO_ONE_DELETED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {
        protected boolean isDeleted;
    }
}