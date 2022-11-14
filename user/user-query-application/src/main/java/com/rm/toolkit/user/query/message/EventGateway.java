package com.rm.toolkit.user.query.message;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.query.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.query.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.query.event.role.RoleEditedEvent;
import com.rm.toolkit.user.query.event.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        switch (event.getType()) {
            case USER_BLOCKED:
                eventHandler.handle((UserBlockedEvent) event);
                break;
            case USER_CREATED:
                eventHandler.handle((UserCreatedEvent) event);
                break;
            case USER_EDITED:
                eventHandler.handle((UserEditedEvent) event);
                break;
            case USER_STATUS_CHANGED:
                eventHandler.handle((UserStatusChangedEvent) event);
                break;
            case USER_DEPARTMENT_CHANGED:
                eventHandler.handle((UserDepartmentChangedEvent) event);
                break;
            case USER_RM_CHANGED:
                eventHandler.handle((UserRmChangedEvent) event);
                break;
            case USER_ROLE_CHANGED:
                eventHandler.handle((UserRoleChangedEvent) event);
                break;
            case CHANGE_USERS_DEPARTMENT:
                eventHandler.handle((ChangeUsersDepartmentEvent) event);
                break;
            case ROLE_CREATED:
                eventHandler.handle((RoleCreatedEvent) event);
                break;
            case ROLE_EDITED:
                eventHandler.handle((RoleEditedEvent) event);
                break;
            case ROLE_DELETED:
                eventHandler.handle((RoleDeletedEvent) event);
                break;
            case DEPARTMENT_CREATED:
                eventHandler.handle((DepartmentCreatedEvent) event);
                break;
            case DEPARTMENT_EDITED:
                eventHandler.handle((DepartmentEditedEvent) event);
                break;
            case DEPARTMENT_DELETED:
                eventHandler.handle((DepartmentDeletedEvent) event);
                break;

        }
    }
}
