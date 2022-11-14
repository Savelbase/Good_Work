package com.rm.toolkit.user.query.repository;

import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.model.UserMediumInfo;
import com.rm.toolkit.user.query.model.UserMinimalInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.resourceManager.id=:resourceManagerId")
    Set<User> findAllByResourceManagerId(@Param("resourceManagerId") String resourceManagerId);

    @Query("SELECT new UserMinimalInfo(u.id, u.firstName, u.lastName, u.avatarPath, u.isRm) FROM User u " +
            "WHERE u.department.id=:departmentId")
    Set<UserMinimalInfo> findAllByDepartmentIdMinimalInfo(@Param("departmentId") String departmentId);

    @Query("SELECT u FROM UserMediumInfo u WHERE u.id=:id")
    Optional<UserMediumInfo> findByIdMediumInfo(@Param("id") String id);

    @Query("SELECT u FROM UserMediumInfo u WHERE" +
            " (upper(u.firstName) like concat (upper(:firstName), '%') and upper(u.lastName) like concat (upper(:lastName), '%')) or" +
            " (upper(u.lastName) like concat (upper(:firstName), '%') and upper(u.firstName) like concat (upper(:lastName), '%'))" +
            " ORDER BY u.lastName, u.firstName  ")
    List<UserMediumInfo> findUserByLastNameAndFirstName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
            , Pageable pageable);

    @Query("SELECT u FROM UserMediumInfo u WHERE upper(u.firstName) like concat (upper(?1), '%') or" +
            " upper(u.lastName) like concat (upper(?1), '%')  ORDER BY u.lastName, u.firstName ")
    List<UserMediumInfo> findUserByFirstName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
            , Pageable pageable);
}

