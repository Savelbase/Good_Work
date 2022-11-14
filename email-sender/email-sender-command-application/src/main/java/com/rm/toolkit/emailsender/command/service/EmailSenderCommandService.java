package com.rm.toolkit.emailsender.command.service;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.command.EmailCommandPayload;
import com.rm.toolkit.emailsender.command.model.Email;
import com.rm.toolkit.emailsender.command.repository.EmailRepository;
import com.rm.toolkit.emailsender.command.repository.WhitelistRepository;
import com.rm.toolkit.emailsender.command.service.impl.JavaMailService;
import com.rm.toolkit.emailsender.command.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderCommandService {
    private final JavaMailService javaMailService;
    private final EmailRepository emailRepository;
    private final WhitelistRepository whitelistRepository;
    private final EmailUtil emailUtil;

    @Transactional
    public void sendEmailToRecipient(EmailCommand command, SubjectType subjectType,
                                       TextType textType, RecipientType recipientType) {

        EmailCommandPayload payload = command.getPayload();
        String recipient = payload.getSubordinateEmail();
        if (recipientType == RecipientType.RESOURCE_MANAGER) {
            recipient = payload.getResourceManagerEmail();
        }
        String subject = subjectType.getSubject();
        if (subjectType == SubjectType.NO_FEEDBACK_PROVIDED_SUBJECT ||
                subjectType == SubjectType.REPEATED_NO_FEEDBACK_PROVIDED_SUBJECT) {
            subject = emailUtil.createSubject(command, subjectType);
        }
        if (whitelistRepository.existsByEmail(recipient)) {
            String message = emailUtil.createText(command, textType);
            Email email = Email.builder()
                    .id(UUID.randomUUID().toString())
                    .emailTo(recipient)
                    .subject(subject)
                    .text(message)
                    .build();

            email.setSendingDateTime(ZonedDateTime.now());
            javaMailService.sendEmail(email);
            emailRepository.save(email);

            log.info("Email с id {} направлен получателю", email.getId());
        }
    }
}
