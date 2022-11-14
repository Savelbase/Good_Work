package com.rm.toolkit.user.command.message.impl;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.message.EventSender;
import com.rm.toolkit.user.command.util.EventUtil;
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

    @Value("${kafka.topic.user}")
    private String userTopic;
    @Value("${kafka.topic.role}")
    private String roleTopic;
    @Value("${kafka.topic.department}")
    private String departmentTopic;

    @Override
    public void send(Event<? extends EventPayload> event) {
        switch (event.getType().getAssociatedObject()) {
            case USER:
                kafkaTemplate.send(userTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
                break;
            case ROLE:
                kafkaTemplate.send(roleTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
                break;
            case DEPARTMENT:
                kafkaTemplate.send(departmentTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
                break;
        }
        kafkaTemplate.flush();
    }
}
