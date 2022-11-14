package com.rm.toolkit.user.query.repository;

import com.rm.toolkit.user.query.model.Department;
import com.rm.toolkit.user.query.model.DepartmentMinimalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    @Query("SELECT new DepartmentMinimalInfo(d.id, d.name) FROM Department d WHERE d.id=:id")
    Optional<DepartmentMinimalInfo> findByIdMinimalInfo(@Param("id") String id);

    @Query("SELECT new DepartmentMinimalInfo(d.id, d.name) FROM Department d")
    List<DepartmentMinimalInfo> findAllMinimalInfo();

    Department findDepartmentByName(@Param("departmentName") String departmentName);
}
