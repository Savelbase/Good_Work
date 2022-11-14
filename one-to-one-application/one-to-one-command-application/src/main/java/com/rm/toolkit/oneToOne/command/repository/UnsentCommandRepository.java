package com.rm.toolkit.oneToOne.command.repository;

import com.rm.toolkit.oneToOne.command.command.UnsentCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnsentCommandRepository extends JpaRepository<UnsentCommand, String> {}