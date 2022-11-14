package com.rm.toolkit.emailsender.command.message;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import org.springframework.messaging.handler.annotation.Payload;

public interface CommandConsumer {

    void handleEmailCommand(@Payload EmailCommand command);
}
