package com.rm.toolkit.oneToOne.command.message.impl;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.message.EventSender;
import com.rm.toolkit.oneToOne.command.util.EventUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class EventSenderKafkaImpl implements EventSender {

    private final KafkaTemplate<Long, Event<? extends EventPayload>> kafkaTemplate;

    @Value("${kafka.topic.one-to-one}")
    private String oneToOneTopic;

    @Value("${kafka.topic.user}")
    private String userTopic;


    @Override
    public void send(Event<? extends EventPayload> event) {
        switch (event.getType().getAssociatedObject()) {
            case ONE_TO_ONE:
                kafkaTemplate.send(oneToOneTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
                break;
        }
        kafkaTemplate.flush();
    }
}
