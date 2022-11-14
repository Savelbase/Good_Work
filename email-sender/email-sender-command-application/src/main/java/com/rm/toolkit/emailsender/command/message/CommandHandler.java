package com.rm.toolkit.emailsender.command.message;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.repository.CommandRepository;
import com.rm.toolkit.emailsender.command.service.EmailSenderCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static com.rm.toolkit.emailsender.command.service.RecipientType.RESOURCE_MANAGER;
import static com.rm.toolkit.emailsender.command.service.RecipientType.SUBORDINATE;
import static com.rm.toolkit.emailsender.command.service.SubjectType.*;
import static com.rm.toolkit.emailsender.command.service.TextType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandHandler {
    private final CommandRepository commandRepository;
    private final EmailSenderCommandService emailSenderCommandService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleCreate(EmailCommand command) {
        emailSenderCommandService.sendEmailToRecipient(command, CREATED_SUBJECT, CREATED_RM_TEXT, RESOURCE_MANAGER);
        emailSenderCommandService.sendEmailToRecipient(command, CREATED_SUBJECT, CREATED_S_TEXT, SUBORDINATE);
        commandRepository.save(command);
        log.info("Email направлены получателям после получения команды {}", command.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleUpdate(EmailCommand command) {
        emailSenderCommandService.sendEmailToRecipient(command, UPDATED_SUBJECT, UPDATED_RM_TEXT, RESOURCE_MANAGER);
        emailSenderCommandService.sendEmailToRecipient(command, UPDATED_SUBJECT, UPDATED_S_TEXT, SUBORDINATE);
        commandRepository.save(command);
        log.info("Email направлены получателям после получения команды {}", command.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleDelete(EmailCommand command) {
        emailSenderCommandService.sendEmailToRecipient(command, DELETED_SUBJECT, DELETED_RM_TEXT, RESOURCE_MANAGER);
        emailSenderCommandService.sendEmailToRecipient(command, DELETED_SUBJECT, DELETED_S_TEXT, SUBORDINATE);
        commandRepository.save(command);
        log.info("Email направлены получателям после получения команды {}", command.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleNoFeedbackProvided(EmailCommand command) {
        emailSenderCommandService.sendEmailToRecipient(command, NO_FEEDBACK_PROVIDED_SUBJECT,
                NO_FEEDBACK_PROVIDED_TEXT, RESOURCE_MANAGER);
        commandRepository.save(command);
        log.info("Email направлены получателю после получения команды {}", command.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handleRepeatedNoFeedbackProvided(EmailCommand command) {
        emailSenderCommandService.sendEmailToRecipient(command, REPEATED_NO_FEEDBACK_PROVIDED_SUBJECT,
                REPEATED_NO_FEEDBACK_PROVIDED_TEXT, RESOURCE_MANAGER);
        commandRepository.save(command);
        log.info("Email направлены получателю после получения команды {}", command.getId());
    }
}
