package com.rm.toolkit.mediaStorage.command.message;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);
}
