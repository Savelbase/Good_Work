package com.rm.toolkit.oneToOne.query.message;

import org.springframework.messaging.handler.annotation.Payload;
import com.rm.toolkit.oneToOne.query.event.Event;
import com.rm.toolkit.oneToOne.query.event.EventPayload;

public interface EventConsumer {
    <T extends Event<? extends EventPayload>> void handleOneToOneEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);

}
