package com.rm.toolkit.auth.message;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    public abstract <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);

    public abstract <T extends Event<? extends EventPayload>> void handleRoleEvent(@Payload T event);
}
