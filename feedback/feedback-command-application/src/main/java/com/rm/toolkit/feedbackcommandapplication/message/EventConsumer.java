package com.rm.toolkit.feedbackcommandapplication.message;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleDepartmentEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleOneToOneEvent(@Payload T event);
}
