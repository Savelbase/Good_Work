package com.rm.toolkit.user.command.test.repository;

import com.rm.toolkit.user.command.model.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentTestRepository extends JpaRepository<Department, String> {

    Optional<Department> findByName(String name);
}
