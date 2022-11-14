package com.rm.toolkit.user.command.test.repository;

import com.rm.toolkit.user.command.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserTestRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u JOIN Department d ON u.departmentId=d.id AND u.departmentId=:departmentId " +
            "AND d.deleted=false")
    Set<User> findAllByDepartmentId(@Param("departmentId") String departmentId);

//    @Query("SELECT u FROM User u JOIN Role r ON u.roleId=r.id WHERE u.roleId=:roleId AND r.deleted=false")
    Set<User> findAllByRoleId(@Param("roleId") String roleId);

    @Query("SELECT u FROM User u JOIN User rm ON u.resourceManagerId=rm.id " +
            "WHERE u.resourceManagerId=:resourceManagerId " +
            "AND NOT rm.status=:#{T(com.rm.toolkit.user.command.model.type.StatusType).DELETED}")
    Set<User> findAllByResourceManagerId(@Param("resourceManagerId") String resourceManagerId);
}
