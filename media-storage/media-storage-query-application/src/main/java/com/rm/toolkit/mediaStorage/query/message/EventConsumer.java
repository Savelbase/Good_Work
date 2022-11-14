package com.rm.toolkit.mediaStorage.query.message;

import com.rm.toolkit.mediaStorage.query.event.Event;
import com.rm.toolkit.mediaStorage.query.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    <T extends Event<? extends EventPayload>> void handleMediaStorageEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);
}
