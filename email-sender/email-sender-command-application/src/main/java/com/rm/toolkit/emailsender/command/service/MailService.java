package com.rm.toolkit.emailsender.command.service;

import com.rm.toolkit.emailsender.command.model.Email;

public interface MailService {

    void sendEmail(Email email);

}