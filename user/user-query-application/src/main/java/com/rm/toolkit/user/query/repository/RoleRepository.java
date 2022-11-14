package com.rm.toolkit.user.query.repository;

import com.rm.toolkit.user.query.model.Role;
import com.rm.toolkit.user.query.model.RoleMinimalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT new RoleMinimalInfo(r.id, r.name) FROM Role r WHERE r.id=:id")
    Optional<RoleMinimalInfo> findByIdMinimalInfo(@Param("id") String id);

    @Query("SELECT new RoleMinimalInfo(r.id, r.name) FROM Role r")
    List<RoleMinimalInfo> findAllMinimalInfo();

    @Query("SELECT new RoleMinimalInfo(r.id, r.name) FROM Role r WHERE r.priority<:priority")
    List<RoleMinimalInfo> findAllByPriorityLessThanMinimalInfo(@Param("priority") int priority);
}
