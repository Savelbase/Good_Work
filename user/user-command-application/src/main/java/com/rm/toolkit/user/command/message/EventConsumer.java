package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    <T extends Event<? extends EventPayload>> void handleAuthEvent(@Payload T event);
}
