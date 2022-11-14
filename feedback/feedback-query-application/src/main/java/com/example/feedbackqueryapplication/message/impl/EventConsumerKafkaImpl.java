package com.example.feedbackqueryapplication.message.impl;

import com.example.feedbackqueryapplication.event.Event;
import com.example.feedbackqueryapplication.event.EventPayload;
import com.example.feedbackqueryapplication.message.EventConsumer;
import com.example.feedbackqueryapplication.message.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumerKafkaImpl implements EventConsumer {

    private final EventGateway eventGateway;

    @Override
    @KafkaListener(topics = "${kafka.topic.feedback}")
    public <T extends Event<? extends EventPayload>> void handleFeedbackEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.user}")
    public <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event) {
        eventGateway.handle(event);
    }
}