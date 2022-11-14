package com.rm.toolkit.oneToOne.command.message.impl;

import com.rm.toolkit.oneToOne.command.command.EmailCommand;
import com.rm.toolkit.oneToOne.command.message.CommandSender;
import com.rm.toolkit.oneToOne.command.util.CommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
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