package com.rm.toolkit.user.command.event.user;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@DiscriminatorValue("CHANGE_USERS_DEPARTMENT")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ChangeUsersDepartmentEvent extends Event<ChangeUsersDepartmentEvent.Payload> {

    public ChangeUsersDepartmentEvent() {
        this.type = EventType.CHANGE_USERS_DEPARTMENT;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected String departmentId;

        protected String departmentName;

        protected String resourceManagerId;

        protected String resourceManagerFirstName;

        protected String resourceManagerLastName;

        protected Set<String> users;

    }
}
