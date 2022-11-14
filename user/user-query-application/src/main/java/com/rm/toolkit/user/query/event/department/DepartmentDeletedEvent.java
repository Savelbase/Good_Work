package com.rm.toolkit.user.query.event.department;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DepartmentDeletedEvent extends Event<DepartmentDeletedEvent.Payload> {

    public DepartmentDeletedEvent() {
        this.type = EventType.DEPARTMENT_EDITED;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
    }
}
