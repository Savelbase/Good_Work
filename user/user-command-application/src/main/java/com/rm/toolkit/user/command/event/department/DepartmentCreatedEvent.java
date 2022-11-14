package com.rm.toolkit.user.command.event.department;


import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DEPARTMENT_CREATED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DepartmentCreatedEvent extends Event<DepartmentCreatedEvent.Payload> {

    public DepartmentCreatedEvent() {
        this.type = EventType.DEPARTMENT_CREATED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected String name;

        protected String headId;

        protected String headFirstName;

        protected String headLastName;

        protected boolean deletable;
    }
}
