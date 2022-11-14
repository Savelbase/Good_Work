package com.rm.toolkit.oneToOne.command.message.impl;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.message.EventConsumer;
import com.rm.toolkit.oneToOne.command.message.EventGateway;
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


}
