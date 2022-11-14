package com.rm.toolkit.auth.message.impl;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.message.EventConsumer;
import com.rm.toolkit.auth.message.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumerKafkaImpl implements EventConsumer {

    private final EventGateway eventGateway;

    @KafkaListener(topics = "${kafka.topic.user}")
    public <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @KafkaListener(topics = "${kafka.topic.role}")
    public <T extends Event<? extends EventPayload>> void handleRoleEvent(@Payload T event) {
        eventGateway.handle(event);
    }
}

