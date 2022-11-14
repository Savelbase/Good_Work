package com.rm.toolkit.emailsender.command.message;

import com.rm.toolkit.emailsender.command.command.CommandType;
import com.rm.toolkit.emailsender.command.command.EmailCommand;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@MockBean(CommandHandler.class)
@ContextConfiguration(classes = CommandGateway.class)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CommandGatewayTest {
    private final CommandHandler commandHandler;
    private final CommandGateway commandGateway;

    private final Supplier<EmailCommand> emailCommandSupplier = () -> {
        EmailCommand emailCommand = new EmailCommand();
        emailCommand.setId("commandId");
        emailCommand.setAuthor("authorId");
        emailCommand.setContext("context");
        emailCommand.setTime(Instant.now());
        return emailCommand;
    };

    @Test
    void handleShouldUseCreateForTypeCreatedOneToOne() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.CREATED_ONE_TO_ONE);
        commandGateway.handle(emailCommand);
        verify(commandHandler).handleCreate(any());
    }

    @Test
    void handleShouldNotUseUpdateAndDeleteForTypeCreatedOneToOne() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.CREATED_ONE_TO_ONE);
        commandGateway.handle(emailCommand);
        verify(commandHandler, never()).handleUpdate(any());
        verify(commandHandler, never()).handleDelete(any());
    }

    @Test
    void handleShouldUseUpdateForTypeUpdatedOneToOne() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.UPDATED_ONE_TO_ONE);
        commandGateway.handle(emailCommand);
        verify(commandHandler).handleUpdate(any());
    }

    @Test
    void handleShouldNotUseCreateAndDeleteForTypeUpdatedOneToOne() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.UPDATED_ONE_TO_ONE);
        commandGateway.handle(emailCommand);
        verify(commandHandler, never()).handleCreate(any());
        verify(commandHandler, never()).handleDelete(any());
    }

    @Test
    void handleShouldUseDeleteForTypeDeletedOneToOne() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.DELETED_ONE_TO_ONE);
        commandGateway.handle(emailCommand);
        verify(commandHandler).handleDelete(any());
    }

    @Test
    void handleShouldNotUseCreateAndUpdateForTypeDeletedOneToOne() {
        EmailCommand emailCommand = emailCommandSupplier.get();
        emailCommand.setType(CommandType.DELETED_ONE_TO_ONE);
        commandGateway.handle(emailCommand);
        verify(commandHandler, never()).handleCreate(any());
        verify(commandHandler, never()).handleUpdate(any());
    }
}