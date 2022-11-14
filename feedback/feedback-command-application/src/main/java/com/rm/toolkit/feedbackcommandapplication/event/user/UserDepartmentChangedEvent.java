package com.rm.toolkit.feedbackcommandapplication.event.user;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserDepartmentChangedEvent extends Event<UserDepartmentChangedEvent.Payload> {

    public UserDepartmentChangedEvent() {
        this.type = EventType.USER_DEPARTMENT_CHANGED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    public static class Payload extends EventPayload {
        protected String departmentId;

        protected String departmentName;

        protected String resourceManagerId;

        protected String resourceManagerFirstName;

        protected String resourceManagerLastName;
    }
}
