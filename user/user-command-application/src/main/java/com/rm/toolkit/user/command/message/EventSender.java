package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;

public interface EventSender {
    void send(Event<? extends EventPayload> event);
}
