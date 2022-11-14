package com.rm.toolkit.emailsender.command.util;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.command.EmailCommandPayload;
import com.rm.toolkit.emailsender.command.repository.CommandRepository;
import com.rm.toolkit.emailsender.command.service.TextType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockBean(CommandRepository.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = EmailUtil.class)
@ExtendWith(SpringExtension.class)
public class EmailUtilTest {

    private final EmailUtil emailUtil;
    private final CommandRepository commandRepository;
    private final List<EmailCommand> emailCommandList = new ArrayList<>();

    private EmailCommand emailCommand;

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
        emailCommand.setPayload(emailCommandPayloadSupplier.get());

    }
    @Test
    void createTextShouldCreateTextForRMAfterCreateOneToOne() {
        assertNotEquals("", emailUtil.createText(emailCommand, TextType.CREATED_RM_TEXT));
    }

    @Test
    void createTextShouldCreateTextForSubordinateAfterCreateOneToOne() {
        assertNotEquals("", emailUtil.createText(emailCommand, TextType.CREATED_S_TEXT));
    }

    @Test
    void createTextShouldCreateTextForRMAfterUpdateOneToOne() {
        emailCommandList.add(emailCommand);
        when(commandRepository.findEmailCommandsByAuthor(any())).thenReturn(emailCommandList);
        assertNotEquals("", emailUtil.createText(emailCommand, TextType.UPDATED_RM_TEXT));
    }

    @Test
    void createTextShouldCreateTextForSubordinateAfterUpdateOneToOne() {
        emailCommandList.add(emailCommand);
        when(commandRepository.findEmailCommandsByAuthor(any())).thenReturn(emailCommandList);
        assertNotEquals("", emailUtil.createText(emailCommand, TextType.UPDATED_S_TEXT));
    }

    @Test
    void createTextShouldCreateTextForRMAfterDeleteOneToOne() {
        emailCommandList.add(emailCommand);
        when(commandRepository.findEmailCommandsByAuthor(any())).thenReturn(emailCommandList);
        assertNotEquals("", emailUtil.createText(emailCommand, TextType.DELETED_RM_TEXT));
    }

    @Test
    void createTextShouldCreateTextForSubordinateAfterDeleteOneToOne() {
        emailCommandList.add(emailCommand);
        when(commandRepository.findEmailCommandsByAuthor(any())).thenReturn(emailCommandList);
        assertNotEquals("", emailUtil.createText(emailCommand, TextType.DELETED_S_TEXT));
    }

    @Test
    void getLastDateTimeOfOneToOneShouldReturnDateTimeOfLastEmailCommand() {
        emailCommandList.add(emailCommand);
        EmailCommand lastEmailCommand = emailCommandSupplier.get();
        lastEmailCommand.setTime(Instant.from(ZonedDateTime.now()));
        lastEmailCommand.setPayload(emailCommandPayloadSupplier.get());
        emailCommandList.add(lastEmailCommand);
        when(commandRepository.findEmailCommandsByAuthor(any())).thenReturn(emailCommandList);
        Instant dateTimeFromLastEmailCommand = lastEmailCommand.getTime();
        Instant dateTimeFromMethod = Instant.from(emailUtil.getLastDateTimeOfOneToOne(emailCommand));
        assertEquals(dateTimeFromLastEmailCommand.getEpochSecond(), dateTimeFromMethod.getEpochSecond());
    }
}