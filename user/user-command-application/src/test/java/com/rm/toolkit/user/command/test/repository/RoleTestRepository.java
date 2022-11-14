package com.rm.toolkit.user.command.test.repository;

import com.rm.toolkit.user.command.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleTestRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(String name);
}
