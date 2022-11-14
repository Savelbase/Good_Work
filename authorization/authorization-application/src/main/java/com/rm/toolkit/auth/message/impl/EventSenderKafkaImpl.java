package com.rm.toolkit.auth.message.impl;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.message.EventSender;
import com.rm.toolkit.auth.util.EventUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSenderKafkaImpl implements EventSender {

    private final KafkaTemplate<Long, Event<? extends EventPayload>> kafkaTemplate;

    @Value("${kafka.topic.auth}")
    private String authTopic;

    @Override
    public void send(Event<? extends EventPayload> event) {
        kafkaTemplate.send(authTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
        kafkaTemplate.flush();
    }
}