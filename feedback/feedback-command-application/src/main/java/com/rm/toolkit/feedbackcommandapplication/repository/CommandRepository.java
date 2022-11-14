package com.rm.toolkit.feedbackcommandapplication.repository;

import com.rm.toolkit.feedbackcommandapplication.command.EmailCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<EmailCommand, String> {
}