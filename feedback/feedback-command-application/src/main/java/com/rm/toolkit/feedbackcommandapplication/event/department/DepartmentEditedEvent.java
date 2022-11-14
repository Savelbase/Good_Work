package com.rm.toolkit.feedbackcommandapplication.event.department;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DEPARTMENT_EDITED")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DepartmentEditedEvent extends Event<DepartmentEditedEvent.Payload> {

    public DepartmentEditedEvent() {
        this.type = EventType.DEPARTMENT_EDITED;
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
    }
}
