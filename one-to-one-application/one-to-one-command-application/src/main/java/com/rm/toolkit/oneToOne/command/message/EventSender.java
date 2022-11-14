package com.rm.toolkit.oneToOne.command.message;


import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;

public interface EventSender {
    void send(Event<? extends EventPayload> event);
}
