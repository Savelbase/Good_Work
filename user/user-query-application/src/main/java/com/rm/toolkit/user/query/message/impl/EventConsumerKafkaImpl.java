package com.rm.toolkit.user.query.message.impl;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.message.EventConsumer;
import com.rm.toolkit.user.query.message.EventGateway;
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

    @KafkaListener(topics = "${kafka.topic.auth}")
    public <T extends Event<? extends EventPayload>> void handleAuthEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @KafkaListener(topics = "${kafka.topic.role}")
    public <T extends Event<? extends EventPayload>> void handleRoleEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @KafkaListener(topics = "${kafka.topic.department}")
    public <T extends Event<? extends EventPayload>> void handleDepartmentEvent(@Payload T event) {
        eventGateway.handle(event);
    }
}
