package com.rm.toolkit.user.command.event.department;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DEPARTMENT_DELETED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DepartmentDeletedEvent extends Event<DepartmentDeletedEvent.Payload> {

    public DepartmentDeletedEvent() {
        this.type = EventType.DEPARTMENT_DELETED;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
    }
}
