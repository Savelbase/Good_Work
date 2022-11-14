package com.rm.toolkit.user.query.event.user;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
