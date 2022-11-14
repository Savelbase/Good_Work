package com.rm.toolkit.feedbackcommandapplication.message.impl;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.message.EventConsumer;
import com.rm.toolkit.feedbackcommandapplication.message.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumerKafkaImpl implements EventConsumer {

    private final EventGateway eventGateway;

    @Override
    @KafkaListener(topics = "${kafka.topic.user}")
    public <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.department}")
    public <T extends Event<? extends EventPayload>> void handleDepartmentEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.one-to-one}")
    public <T extends Event<? extends EventPayload>> void handleOneToOneEvent(@Payload T event) {
        eventGateway.handle(event);
    }
}
