package com.rm.toolkit.emailsender.command.message;

import com.rm.toolkit.emailsender.command.command.CommandType;
import com.rm.toolkit.emailsender.command.command.EmailCommand;
import com.rm.toolkit.emailsender.command.repository.CommandRepository;
import com.rm.toolkit.emailsender.command.service.EmailSenderCommandService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;


@MockBeans({@MockBean(EmailSenderCommandService.class),
            @MockBean(CommandRepository.class)})
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = CommandHandler.class)
@ExtendWith(SpringExtension.class)
public class CommandHandlerTest {

    private final CommandRepository commandRepository;
    private final EmailSenderCommandService emailSenderCommandService;
    private final CommandHandler commandHandler;

    private final Supplier<EmailCommand> emailCommandSupplier = () -> {
        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setId("commandId");
        emailCommand.setAuthor("authorId");
        emailCommand.setContext("context");
        emailCommand.setTime(Instant.now());
        return emailCommand;
    };

    @Test
    void handlerCreateShouldUseRepositoryAndService() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.CREATED_ONE_TO_ONE);
        commandHandler.handleCreate(emailCommand);
        verify(commandRepository).save(any());
        verify(emailSenderCommandService, times(2)).sendEmailToRecipient(any(), any(), any(), any());
    }

    @Test
    void handlerUpdateShouldUseRepositoryAndService() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.UPDATED_ONE_TO_ONE);
        commandHandler.handleUpdate(emailCommand);
        verify(commandRepository).save(any());
        verify(emailSenderCommandService, times(2)).sendEmailToRecipient(any(), any(), any(), any());
    }

    @Test
    void handlerDeleteShouldUseRepositoryAndService() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.DELETED_ONE_TO_ONE);
        commandHandler.handleDelete(emailCommand);
        verify(commandRepository).save(any());
        verify(emailSenderCommandService, times(2)).sendEmailToRecipient(any(), any(), any(), any());
    }
}