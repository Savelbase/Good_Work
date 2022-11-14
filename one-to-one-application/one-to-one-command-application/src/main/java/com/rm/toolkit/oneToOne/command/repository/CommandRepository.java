package com.rm.toolkit.oneToOne.command.repository;

import com.rm.toolkit.oneToOne.command.command.EmailCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends JpaRepository<EmailCommand, String> {

}