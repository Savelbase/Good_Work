package com.rm.toolkit.user.query.repository;

import com.rm.toolkit.user.query.dto.query.UserQuery;
import com.rm.toolkit.user.query.model.UserMediumInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomUserRepository {

    Page<UserMediumInfo> findAllByUserQuery(UserQuery query, Pageable pageable);

    List<UserMediumInfo> findUserByLastNameAndFirstName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            Pageable pageable);

    List<UserMediumInfo> findUserByFirstName(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            Pageable pageable);

}
