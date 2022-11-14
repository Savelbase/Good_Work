package com.rm.toolkit.user.command.repository;

import com.rm.toolkit.user.command.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    boolean existsByNameIgnoreCase(String name);

    Set<Department> findAllByHeadId(String headId);

    Department findDepartmentById(String departmentId);
}
