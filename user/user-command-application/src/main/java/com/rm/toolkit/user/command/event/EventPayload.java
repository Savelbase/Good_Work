package com.rm.toolkit.user.command.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.user.command.event.auth.UserMaxLoginAttemptsReachedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.command.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.command.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.command.event.role.RoleEditedEvent;
import com.rm.toolkit.user.command.event.user.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserMaxLoginAttemptsReachedEvent.Payload.class, name = "USER_MAX_LOGIN_ATTEMPTS_REACHED"),
        @JsonSubTypes.Type(value = UserBlockedEvent.Payload.class, name = "USER_BLOCKED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.Payload.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.Payload.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.Payload.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = UserRmChangedEvent.Payload.class, name = "USER_RM_CHANGED"),
        @JsonSubTypes.Type(value = UserRoleChangedEvent.Payload.class, name = "USER_ROLE_CHANGED"),
        @JsonSubTypes.Type(value = UserStatusChangedEvent.Payload.class, name = "USER_STATUS_CHANGED"),
        @JsonSubTypes.Type(value = RoleCreatedEvent.Payload.class, name = "ROLE_CREATED"),
        @JsonSubTypes.Type(value = RoleEditedEvent.Payload.class, name = "ROLE_EDITED"),
        @JsonSubTypes.Type(value = RoleDeletedEvent.Payload.class, name = "ROLE_DELETED"),
        @JsonSubTypes.Type(value = DepartmentCreatedEvent.Payload.class, name = "DEPARTMENT_CREATED"),
        @JsonSubTypes.Type(value = DepartmentEditedEvent.Payload.class, name = "DEPARTMENT_EDITED"),
        @JsonSubTypes.Type(value = DepartmentDeletedEvent.Payload.class, name = "DEPARTMENT_DELETED"),
        @JsonSubTypes.Type(value = ChangeUsersDepartmentEvent.Payload.class, name = "CHANGE_USERS_DEPARTMENT")
})
@Data
@NoArgsConstructor
public class EventPayload {

}
