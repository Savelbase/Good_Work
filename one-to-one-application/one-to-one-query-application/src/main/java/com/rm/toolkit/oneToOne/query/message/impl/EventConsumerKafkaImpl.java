package com.rm.toolkit.oneToOne.query.message.impl;

import com.rm.toolkit.oneToOne.query.event.Event;
import com.rm.toolkit.oneToOne.query.message.EventConsumer;
import com.rm.toolkit.oneToOne.query.message.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.rm.toolkit.oneToOne.query.event.EventPayload;


@RequiredArgsConstructor
@Component
public class EventConsumerKafkaImpl implements EventConsumer {

    private final EventGateway eventGateway;

    @KafkaListener(topics = "${kafka.topic.one-to-one}")
    public <T extends Event<? extends EventPayload>> void handleOneToOneEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @KafkaListener(topics = "${kafka.topic.user}")
    public <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event) {
        eventGateway.handle(event);
    }

}
