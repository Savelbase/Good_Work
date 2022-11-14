package com.rm.toolkit.mediaStorage.command.message.impl;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.message.EventConsumer;
import com.rm.toolkit.mediaStorage.command.message.EventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventConsumerKafkaImpl implements EventConsumer {

    private final EventGateway eventGateway;

    @KafkaListener(topics = "${kafka.topic.user}")
    public <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event) {
        eventGateway.handle(event);
    }
}
