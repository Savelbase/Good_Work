package com.rm.toolkit.feedbackcommandapplication.message.impl;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.message.EventSender;
import com.rm.toolkit.feedbackcommandapplication.util.EventUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventSenderKafkaImpl implements EventSender {

    private final KafkaTemplate<Long, Event<? extends EventPayload>> kafkaTemplate;

    @Value("${kafka.topic.feedback}")
    private String feedbackTopic;

    @Override
    public void send(Event<? extends EventPayload> event) {
        switch (event.getType().getAssociatedObject()) {
            case FEEDBACK:
                kafkaTemplate.send(feedbackTopic, EventUtil.uuidStringToLong(event.getEntityId()), event);
                break;
        }

        kafkaTemplate.flush();
    }
}

