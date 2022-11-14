package com.rm.toolkit.user.command.repository;

import com.rm.toolkit.user.command.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {

    @Override
    @NonNull
    @Query("SELECT u FROM User u WHERE u.id=:id")
    Optional<User> findById(@Param("id") @NonNull String id);

    @Modifying
    @Query("SELECT u FROM User u WHERE u.id IN(:id)")
    Set<User> findUsersById(@Param("id") Set<String> id);

    boolean existsByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u JOIN Role r ON u.roleId=r.id WHERE u.roleId=:roleId AND r.deleted=false")
    Set<User> findAllByRoleId(@Param("roleId") String roleId);

    @Query("SELECT u FROM User u JOIN Department d ON u.departmentId=d.id AND u.departmentId=:departmentId " +
            "AND d.deleted=false")
    Set<User> findAllByDepartmentId(@Param("departmentId") String departmentId);

    @Query("SELECT u FROM User u JOIN User rm ON u.resourceManagerId=rm.id " +
            "WHERE u.resourceManagerId=:resourceManagerId " +
            "AND NOT rm.status=:#{T(com.rm.toolkit.user.command.model.type.StatusType).DELETED}")
    Set<User> findAllByResourceManagerId(@Param("resourceManagerId") String resourceManagerId);

    @Query("SELECT u FROM User u JOIN User rm ON u.resourceManagerId=rm.id JOIN Role r ON u.roleId=r.id " +
            "WHERE rm.roleId=:resourceManagerRoleId AND r.priority>=:minPriority")
    Set<User> findAllByResourceManagerRoleIdAndRolePriorityBetween(@Param("resourceManagerRoleId") String resourceManagerRoleId,
                                                                   @Param("minPriority") Integer minPriority);

    @Query("SELECT u FROM User u JOIN Department d ON u.id=d.headId JOIN Role r ON u.roleId=:roleId AND r.id=u.roleId " +
            "AND r.deleted=false")
    Set<User> findHeadsWithRoleId(@Param("roleId") String roleId);

    @Modifying
    @Query("UPDATE User SET departmentId=:department where id=:userId")
    void updateEmployeesDepartment (@Param("department") String department, @Param("userId") String userId);

}
