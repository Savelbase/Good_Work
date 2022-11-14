package com.rm.toolkit.user.command.event.user;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("USER_DEPARTMENT_CHANGED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserDepartmentChangedEvent extends Event<UserDepartmentChangedEvent.Payload> {

    public UserDepartmentChangedEvent() {
        this.type = EventType.USER_DEPARTMENT_CHANGED;
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
    }
}
