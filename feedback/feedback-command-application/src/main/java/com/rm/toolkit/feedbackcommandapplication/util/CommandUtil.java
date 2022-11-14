package com.rm.toolkit.feedbackcommandapplication.util;

import com.rm.toolkit.feedbackcommandapplication.command.Command;
import com.rm.toolkit.feedbackcommandapplication.command.CommandType;
import com.rm.toolkit.feedbackcommandapplication.command.EmailCommandPayload;
import com.rm.toolkit.feedbackcommandapplication.model.OneToOne;
import com.rm.toolkit.feedbackcommandapplication.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CommandUtil {

    @Value("${spring.application.name}")
    private String context;

    public static Long uuidStringToLong(String uuid) {
        return UUID.fromString(uuid).getMostSignificantBits() & Long.MAX_VALUE;
    }

    /**
     * Заполнить все поля Command.
     *
     * @param command       команда
     * @param payload       payload команды
     */
    public <T, S extends Command<T>> void populateCommandFields(S command, T payload) {
        command.setId(UUID.randomUUID().toString());
        command.setContext(context);
        command.setTime(Instant.now());
        command.setPayload(payload);
    }

    public EmailCommandPayload createEmailCommandPayload(User subordinate, User resourceManager,
                                                         OneToOne oneToOne, CommandType type) {
        return EmailCommandPayload.builder()
                .type(type)
                .oneToOneId(oneToOne.getId())
                .subordinateFirstName(subordinate.getFirstName())
                .subordinateLastName(subordinate.getLastName())
                .subordinateEmail(subordinate.getEmail())
                .resourceManagerFirstName(resourceManager.getFirstName())
                .resourceManagerLastName(resourceManager.getLastName())
                .resourceManagerEmail(resourceManager.getEmail())
                .dateTime(oneToOne.getDateTime())
                .isDeleted(oneToOne.isDeleted())
                .build();
    }
}