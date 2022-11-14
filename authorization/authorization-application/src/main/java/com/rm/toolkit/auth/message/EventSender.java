package com.rm.toolkit.auth.message;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;

public interface EventSender {
    void send(Event<? extends EventPayload> event);
}
