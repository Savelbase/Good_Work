package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import com.rm.toolkit.user.command.event.auth.UserMaxLoginAttemptsReachedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        if (EventType.USER_MAX_LOGIN_ATTEMPTS_REACHED.equals(event.getType())) {
            eventHandler.handle((UserMaxLoginAttemptsReachedEvent) event);
        }
    }
}
