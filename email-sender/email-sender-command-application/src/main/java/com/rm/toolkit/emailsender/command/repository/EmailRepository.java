package com.rm.toolkit.emailsender.command.repository;

import com.rm.toolkit.emailsender.command.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {
}