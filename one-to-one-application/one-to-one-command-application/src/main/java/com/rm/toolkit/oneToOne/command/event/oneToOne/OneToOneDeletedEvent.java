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
@DiscriminatorValue("ONE_TO_ONE_DELETED")
public class OneToOneDeletedEvent extends Event<OneToOneDeletedEvent.Payload> {

    public OneToOneDeletedEvent() {
        this.type = EventType.ONE_TO_ONE_DELETED;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload extends EventPayload {

        protected boolean isDeleted;
    }
}