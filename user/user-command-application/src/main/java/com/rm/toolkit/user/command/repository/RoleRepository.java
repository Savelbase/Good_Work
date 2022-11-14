package com.rm.toolkit.user.command.repository;

import com.rm.toolkit.user.command.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface
RoleRepository extends JpaRepository<Role, String> {

    boolean existsByNameIgnoreCase(String name);
}
