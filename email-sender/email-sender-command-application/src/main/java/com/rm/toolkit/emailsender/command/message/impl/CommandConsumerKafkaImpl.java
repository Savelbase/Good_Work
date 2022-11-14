package com.rm.toolkit.emailsender.command.message.impl;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.message.CommandConsumer;
import com.rm.toolkit.emailsender.command.message.CommandGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandConsumerKafkaImpl implements CommandConsumer {

    private final CommandGateway commandGateway;

    @Override
    @KafkaListener(topics = "${kafka.topic.email-sender}")
    public void handleEmailCommand(@Payload EmailCommand command) {
        commandGateway.handle(command);
    }

}
