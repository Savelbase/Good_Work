package com.rm.toolkit.user.query.message;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleAuthEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleRoleEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleDepartmentEvent(@Payload T event);
}
