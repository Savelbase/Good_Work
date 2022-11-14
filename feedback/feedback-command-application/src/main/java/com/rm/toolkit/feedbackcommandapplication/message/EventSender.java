package com.rm.toolkit.feedbackcommandapplication.message;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;

public interface EventSender {
    void send(Event<? extends EventPayload> event);
}
