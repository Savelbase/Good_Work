package com.rm.toolkit.oneToOne.command.message;


import com.rm.toolkit.oneToOne.command.command.EmailCommand;

public interface CommandSender {
    void send(EmailCommand command);
}