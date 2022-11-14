package com.rm.toolkit.feedbackcommandapplication.event.department;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DEPARTMENT_DELETED")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DepartmentDeletedEvent extends Event<DepartmentDeletedEvent.Payload> {

    public DepartmentDeletedEvent() {
        this.type = EventType.DEPARTMENT_DELETED;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {
    }
}
