package com.example.feedbackqueryapplication.message;

import com.example.feedbackqueryapplication.event.Event;
import com.example.feedbackqueryapplication.event.EventPayload;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    <T extends Event<? extends EventPayload>> void handleFeedbackEvent(@Payload T event);

    <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event);
}
