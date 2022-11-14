package com.rm.toolkit.feedbackcommandapplication.message.impl;

import com.rm.toolkit.feedbackcommandapplication.command.EmailCommand;
import com.rm.toolkit.feedbackcommandapplication.message.CommandSender;
import com.rm.toolkit.feedbackcommandapplication.util.CommandUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandSenderKafkaImpl implements CommandSender {

    private final KafkaTemplate<Long, EmailCommand> kafkaTemplate;

    @Value("${kafka.topic.email-sender}")
    private String emailTopic;

    @Override
    public void send(EmailCommand command) {
        kafkaTemplate.send(emailTopic, CommandUtil.uuidStringToLong(command.getId()), command);
        kafkaTemplate.flush();
    }
}