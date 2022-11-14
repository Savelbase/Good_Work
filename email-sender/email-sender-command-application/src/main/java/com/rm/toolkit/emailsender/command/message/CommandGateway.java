package com.rm.toolkit.emailsender.command.message;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommandGateway {

    private final CommandHandler commandHandler;

    public void handle(EmailCommand command) {
        switch (command.getType()) {
            case CREATED_ONE_TO_ONE:
                commandHandler.handleCreate(command);
                break;
            case UPDATED_ONE_TO_ONE:
                commandHandler.handleUpdate(command);
                break;
            case DELETED_ONE_TO_ONE:
                commandHandler.handleDelete(command);
                break;
            case NO_FEEDBACK_PROVIDED:
                commandHandler.handleNoFeedbackProvided(command);
                break;
            case REPEATED_NO_FEEDBACK_PROVIDED:
                commandHandler.handleRepeatedNoFeedbackProvided(command);
                break;
        }
    }
}
