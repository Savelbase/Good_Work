package com.rm.toolkit.feedbackcommandapplication.message;

import com.rm.toolkit.feedbackcommandapplication.command.EmailCommand;

public interface CommandSender {
    void send(EmailCommand command);
}