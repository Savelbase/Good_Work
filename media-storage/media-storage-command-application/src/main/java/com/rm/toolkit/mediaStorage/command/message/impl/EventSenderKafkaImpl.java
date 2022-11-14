package com.rm.toolkit.mediaStorage.command.message.impl;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.message.EventSender;
import com.rm.toolkit.mediaStorage.command.util.EventUtil;
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

    @Value("${kafka.topic.media-storage}")
    private String userTopic;

    @Override
    public void send(Event<? extends EventPayload> event) {
        switch (event.getType().getAssociatedObject()) {
            case MEDIA_FILE:
                kafkaTemplate.send(userTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
                break;
        }
        kafkaTemplate.flush();
    }
}
