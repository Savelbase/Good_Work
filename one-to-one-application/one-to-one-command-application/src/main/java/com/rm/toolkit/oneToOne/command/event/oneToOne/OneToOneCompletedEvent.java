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

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("ONE_TO_ONE_COMPLETED")
public class OneToOneCompletedEvent extends Event<OneToOneCompletedEvent.Payload> {

    public OneToOneCompletedEvent() {
        this.type = EventType.ONE_TO_ONE_COMPLETED;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload extends EventPayload {

        protected boolean isOver;
    }
}