package com.rm.toolkit.comment.command.message;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;

public interface EventSender {
    void send(Event<? extends EventPayload> event);
}
