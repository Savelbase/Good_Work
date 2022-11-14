package com.rm.toolkit.comment.query.message;

import com.rm.toolkit.comment.query.event.Event;
import com.rm.toolkit.comment.query.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    <T extends Event<? extends EventPayload>>
    void handleCommentEvent(@Payload T event);
}
