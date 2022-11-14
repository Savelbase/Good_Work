package com.rm.toolkit.emailsender.command.util;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.command.EmailCommandPayload;
import com.rm.toolkit.emailsender.command.repository.CommandRepository;
import com.rm.toolkit.emailsender.command.service.SubjectType;
import com.rm.toolkit.emailsender.command.service.TextType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.format.FormatStyle.MEDIUM;
import static java.time.format.FormatStyle.SHORT;

@Component
@AllArgsConstructor
public class EmailUtil {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(MEDIUM, SHORT);
    private final CommandRepository commandRepository;

    public String createText(EmailCommand command, TextType textType) {
        EmailCommandPayload payload = command.getPayload();

        User subordinate = new User();
        subordinate.setFirstName(payload.getSubordinateFirstName());
        subordinate.setLastName(payload.getSubordinateLastName());

        User resourceManager = new User();
        resourceManager.setFirstName(payload.getResourceManagerFirstName());
        resourceManager.setLastName(payload.getResourceManagerLastName());

        String text = "";
        switch (textType) {
            case CREATED_RM_TEXT:
                text = String.format(textType.getSubject(),
                        resourceManager.getFirstName(), resourceManager.getLastName(),
                        payload.getDateTime().format(dateTimeFormatter),
                        subordinate.getFirstName(), subordinate.getLastName(),
                        payload.getOneToOneId(), payload.getComment());
                break;
            case CREATED_S_TEXT:
                text = String.format(textType.getSubject(),
                        subordinate.getFirstName(), subordinate.getLastName(),
                        payload.getDateTime().format(dateTimeFormatter),
                        resourceManager.getFirstName(), resourceManager.getLastName(),
                        payload.getOneToOneId(), payload.getComment());
                break;
            case UPDATED_RM_TEXT:
                text = String.format(textType.getSubject(),
                        resourceManager.getFirstName(), resourceManager.getLastName(),
                        subordinate.getFirstName(), subordinate.getLastName(),
                        getLastDateTimeOfOneToOne(command).format(dateTimeFormatter), payload.getDateTime().format(dateTimeFormatter),
                        payload.getOneToOneId(), payload.getComment());
                break;
            case UPDATED_S_TEXT:
                text = String.format(textType.getSubject(),
                        subordinate.getFirstName(), subordinate.getLastName(),
                        resourceManager.getFirstName(), resourceManager.getLastName(),
                        getLastDateTimeOfOneToOne(command).format(dateTimeFormatter), payload.getDateTime().format(dateTimeFormatter),
                        payload.getOneToOneId(), payload.getComment());
                break;
            case DELETED_RM_TEXT:
                text = String.format(textType.getSubject(),
                        resourceManager.getFirstName(), resourceManager.getLastName(),
                        subordinate.getFirstName(), subordinate.getLastName(),
                        getLastDateTimeOfOneToOne(command).format(dateTimeFormatter),
                        payload.getOneToOneId(), payload.getComment());
                break;
            case DELETED_S_TEXT:
                text = String.format(textType.getSubject(),
                        subordinate.getFirstName(), subordinate.getLastName(),
                        resourceManager.getFirstName(), resourceManager.getLastName(),
                        getLastDateTimeOfOneToOne(command).format(dateTimeFormatter),
                        payload.getOneToOneId(), payload.getComment());
                break;
            case NO_FEEDBACK_PROVIDED_TEXT:
            case REPEATED_NO_FEEDBACK_PROVIDED_TEXT:
                text = String.format(subordinate.getFirstName(), subordinate.getLastName());
                break;
        }
        return text;
    }

    public String createSubject(EmailCommand command, SubjectType subjectType) {
        EmailCommandPayload payload = command.getPayload();

        User subordinate = new User();
        subordinate.setFirstName(payload.getSubordinateFirstName());
        subordinate.setLastName(payload.getSubordinateLastName());

        String subject = "";
        switch (subjectType) {
            case NO_FEEDBACK_PROVIDED_SUBJECT:
            case REPEATED_NO_FEEDBACK_PROVIDED_SUBJECT:
                subject = String.format(subordinate.getFirstName(), subordinate.getLastName());
                break;
        }
        return subject;
    }

    public ZonedDateTime getLastDateTimeOfOneToOne(EmailCommand command) {
        return commandRepository.findEmailCommandsByAuthor(command.getAuthor()).stream()
                .filter(c -> Objects.equals(c.getPayload().getOneToOneId(), command.getPayload().getOneToOneId()))
                .min((EmailCommand c1, EmailCommand c2) -> {
                    long sec1 = c1.getTime().getEpochSecond();
                    long sec2 = c2.getTime().getEpochSecond();
                    return (int) (sec2 - sec1);
                }).get().getPayload().getDateTime();
    }

    @Getter
    @Setter
    private class User {
        private String firstName;
        private String lastName;
    }
}