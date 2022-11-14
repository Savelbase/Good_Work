package com.rm.toolkit.emailsender.command.repository;

import com.rm.toolkit.emailsender.command.model.WhitelistEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface WhitelistRepository extends JpaRepository<WhitelistEmail, String> {

    boolean existsByEmail(String email);

    @Query("SELECT e.email FROM WhitelistEmail e")
    Set<String> findAllEmails();
}