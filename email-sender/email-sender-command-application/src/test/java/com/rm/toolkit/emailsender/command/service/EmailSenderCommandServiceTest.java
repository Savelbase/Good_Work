package com.rm.toolkit.emailsender.command.service;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.command.EmailCommandPayload;
import com.rm.toolkit.emailsender.command.repository.EmailRepository;
import com.rm.toolkit.emailsender.command.repository.WhitelistRepository;
import com.rm.toolkit.emailsender.command.service.impl.JavaMailService;
import com.rm.toolkit.emailsender.command.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@MockBeans({@MockBean(JavaMailService.class),
            @MockBean(EmailRepository.class),
            @MockBean(WhitelistRepository.class),
            @MockBean(EmailUtil.class)
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = EmailSenderCommandService.class)
@ExtendWith(SpringExtension.class)
class EmailSenderCommandServiceTest {

    private final JavaMailService javaMailService;
    private final EmailRepository emailRepository;
    private final WhitelistRepository whitelistRepository;
    private final EmailUtil emailUtil;
    private EmailCommand emailCommand;

    private final EmailSenderCommandService emailSenderCommandService;

    private final Supplier<EmailCommandPayload> emailCommandPayloadSupplier = () -> EmailCommandPayload.builder()
            .subordinateFirstName("subordinateFirstName")
            .subordinateLastName("subordinateLastName")
            .subordinateEmail("subordinate@email.com")
            .resourceManagerFirstName("resourceManagerFirstName")
            .resourceManagerLastName("resourceManagerLastName")
            .resourceManagerEmail("resourceManager@email.com")
            .dateTime(ZonedDateTime.now())
            .comment("comment")
            .build();

    private final Supplier<EmailCommand> emailCommandSupplier = () -> {
        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setId("commandId");
        emailCommand.setAuthor("authorId");
        emailCommand.setContext("context");
        emailCommand.setTime(Instant.now());
        return emailCommand;
    };

    @BeforeEach
    void preparationOfData() {
        emailCommand = emailCommandSupplier.get();
        EmailCommandPayload emailCommandPayload = emailCommandPayloadSupplier.get();
        emailCommand.setPayload(emailCommandPayload);
        when(whitelistRepository.existsByEmail(any())).thenReturn(true);
    }

    @Test
    void sendEmailToRecipientShouldUseEmailUtilCreateText() {
        emailSenderCommandService.sendEmailToRecipient(emailCommand, SubjectType.CREATED_SUBJECT,
                TextType.CREATED_RM_TEXT, RecipientType.RESOURCE_MANAGER);
        verify(emailUtil).createText(any(), any());
    }

    @Test
    void sendEmailToRecipientShouldUseJavaMailService() {
        emailSenderCommandService.sendEmailToRecipient(emailCommand, SubjectType.CREATED_SUBJECT,
                TextType.CREATED_RM_TEXT, RecipientType.RESOURCE_MANAGER);
        verify(javaMailService).sendEmail(any());
    }

    @Test
    void sendEmailToRecipientShouldUseEmailRepository() {
        emailSenderCommandService.sendEmailToRecipient(emailCommand, SubjectType.CREATED_SUBJECT,
                TextType.CREATED_RM_TEXT, RecipientType.RESOURCE_MANAGER);
        verify(emailRepository).save(any());
    }

    @Test
    void sendMailShouldNotDoAnythingIfEmailAbsentsInWhitelist() {
        when(whitelistRepository.existsByEmail(any())).thenReturn(false);
        verify(emailUtil, never()).createText(any(), any());
        verify(javaMailService, never()).sendEmail(any());
        verify(emailRepository, never()).save(any());
    }


}