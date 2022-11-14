package com.rm.toolkit.auth.message;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.role.RoleCreatedEvent;
import com.rm.toolkit.auth.event.role.RoleDeletedEvent;
import com.rm.toolkit.auth.event.role.RoleEditedEvent;
import com.rm.toolkit.auth.event.user.UserBlockedEvent;
import com.rm.toolkit.auth.event.user.UserCreatedEvent;
import com.rm.toolkit.auth.event.user.UserEditedEvent;
import com.rm.toolkit.auth.event.user.UserStatusChangedEvent;
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
            case ROLE_CREATED:
                eventHandler.handle((RoleCreatedEvent) event);
                break;
            case ROLE_EDITED:
                eventHandler.handle((RoleEditedEvent) event);
                break;
            case ROLE_DELETED:
                eventHandler.handle((RoleDeletedEvent) event);
                break;
        }
    }
}
