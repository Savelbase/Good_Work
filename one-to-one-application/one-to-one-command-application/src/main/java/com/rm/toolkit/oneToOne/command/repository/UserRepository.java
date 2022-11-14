package com.rm.toolkit.oneToOne.command.repository;

import com.rm.toolkit.oneToOne.command.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
