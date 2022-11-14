package com.rm.toolkit.oneToOne.command.event.oneToOne;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("ONE_TO_ONE_UPDATED")
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