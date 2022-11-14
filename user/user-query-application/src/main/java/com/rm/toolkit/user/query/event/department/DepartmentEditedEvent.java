package com.rm.toolkit.user.query.event.department;


import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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
