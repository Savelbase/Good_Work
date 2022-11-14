package com.rm.toolkit.auth.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.auth.event.auth.UserMaxLoginAttemptsReachedEvent;
import com.rm.toolkit.auth.event.role.RoleCreatedEvent;
import com.rm.toolkit.auth.event.role.RoleDeletedEvent;
import com.rm.toolkit.auth.event.role.RoleEditedEvent;
import com.rm.toolkit.auth.event.user.*;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserMaxLoginAttemptsReachedEvent.Payload.class, name = "USER_MAX_LOGIN_ATTEMPTS_REACHED"),
        @JsonSubTypes.Type(value = UserBlockedEvent.Payload.class, name = "USER_BLOCKED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.Payload.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.Payload.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserRoleChangedEvent.Payload.class, name = "USER_ROLE_CHANGED"),
        @JsonSubTypes.Type(value = UserStatusChangedEvent.Payload.class, name = "USER_STATUS_CHANGED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.Payload.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = RoleCreatedEvent.Payload.class, name = "ROLE_CREATED"),
        @JsonSubTypes.Type(value = RoleEditedEvent.Payload.class, name = "ROLE_EDITED"),
        @JsonSubTypes.Type(value = RoleDeletedEvent.Payload.class, name = "ROLE_DELETED")
})
@NoArgsConstructor
public class EventPayload {
}
