package com.rm.toolkit.comment.query.message.impl;

import com.rm.toolkit.comment.query.event.Event;
import com.rm.toolkit.comment.query.event.EventPayload;
import com.rm.toolkit.comment.query.message.EventConsumer;
import com.rm.toolkit.comment.query.message.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumerKafkaImpl implements EventConsumer {

    private final EventGateway eventGateway;

    @Override
    @KafkaListener(topics = "${kafka.topic.comment}")
    public <T extends Event<? extends EventPayload>> void handleCommentEvent(T event) {
        eventGateway.handle(event);
    }
}
