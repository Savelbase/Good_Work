package com.rm.toolkit.comment.command.message.impl;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.message.EventSender;
import com.rm.toolkit.comment.command.util.EventUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSenderKafkaImpl implements EventSender {

    private final KafkaTemplate<Long, Event<? extends EventPayload>> kafkaTemplate;

    @Value("${kafka.topic.comment}")
    private String commentTopic;

    @Override
    public void send(Event<? extends EventPayload> event) {
        kafkaTemplate.send(commentTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
        kafkaTemplate.flush();
    }
}

