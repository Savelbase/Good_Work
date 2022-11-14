package com.rm.toolkit.emailsender.command.repository;

import com.rm.toolkit.emailsender.command.command.EmailCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<EmailCommand, String> {
    List<EmailCommand> findEmailCommandsByAuthor(String author);
}