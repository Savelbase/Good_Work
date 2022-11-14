package com.rm.toolkit.mediaStorage.command.message;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.event.EventType;
import com.rm.toolkit.mediaStorage.command.event.user.UserCreatedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserEditedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        if (EventType.USER_CREATED.equals(event.getType())) {
            eventHandler.handle((UserCreatedEvent) event);
        } else if (EventType.USER_EDITED.equals(event.getType())) {
            eventHandler.handle((UserEditedEvent) event);
        }
    }
}
