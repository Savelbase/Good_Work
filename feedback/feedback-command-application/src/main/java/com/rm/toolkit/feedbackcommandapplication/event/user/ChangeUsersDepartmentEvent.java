package com.rm.toolkit.feedbackcommandapplication.event.user;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ChangeUsersDepartmentEvent extends Event<ChangeUsersDepartmentEvent.Payload> {

    public ChangeUsersDepartmentEvent() {
        this.type = EventType.CHANGE_USERS_DEPARTMENT;
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

        protected Set<String> users;
    }
}
