package com.rm.toolkit.mediaStorage.query.message.impl;

import com.rm.toolkit.mediaStorage.query.event.Event;
import com.rm.toolkit.mediaStorage.query.event.EventPayload;
import com.rm.toolkit.mediaStorage.query.message.EventConsumer;
import com.rm.toolkit.mediaStorage.query.message.EventGateway;
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

    @KafkaListener(topics = "${kafka.topic.media-storage}")
    public <T extends Event<? extends EventPayload>> void handleMediaStorageEvent(@Payload T event) {
        eventGateway.handle(event);
    }

    @KafkaListener(topics = "${kafka.topic.user}")
    public <T extends Event<? extends EventPayload>> void handleUserEvent(@Payload T event) {
        eventGateway.handle(event);
    }

}
