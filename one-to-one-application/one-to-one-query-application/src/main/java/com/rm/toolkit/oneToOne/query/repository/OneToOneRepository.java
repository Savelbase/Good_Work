package com.rm.toolkit.oneToOne.query.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rm.toolkit.oneToOne.query.model.OneToOne;

import java.util.Optional;

@Repository
public interface OneToOneRepository extends PagingAndSortingRepository<OneToOne, String>,
        JpaSpecificationExecutor<OneToOne> {

    @Query("SELECT o FROM OneToOne o WHERE o.userId = :userId AND o.isOver = true AND o.isDeleted = false")
    Page<OneToOne> findAllByUserIdAndIsOverTrueAndDeletedFalse(@Param("userId")String userId, Pageable pageable);

    @Query("SELECT o FROM OneToOne o WHERE o.userId = :userId AND o.isOver = false AND o.isDeleted = false")
    Page<OneToOne> findAllByUserIdAndIsOverFalseAndDeletedFalse(@Param("userId")String userId, Pageable pageable);

    @Query("SELECT o FROM OneToOne o WHERE o.resourceManagerId = :rmId AND o.isOver = true AND o.isDeleted = false")
    Page<OneToOne> findAllByResourceManagerIdAndIsOverTrueAndDeletedFalse(@Param("rmId")String rmId, Pageable pageable);

    @Query("SELECT o FROM OneToOne o WHERE o.resourceManagerId = :rmId AND o.isOver = false AND o.isDeleted = false")
    Page<OneToOne> findAllByResourceManagerIdAndIsOverFalseAndDeletedFalse(@Param("rmId")String rmId, Pageable pageable);

    boolean existsByUserId(String userId);
    boolean existsByResourceManagerId(String rmId);

    @Override
    @Query("SELECT o FROM OneToOne o WHERE o.isDeleted = false")
    Iterable<OneToOne> findAll();

    @Override
    @Query("SELECT o FROM OneToOne o WHERE o.id = ?1 AND o.isDeleted = false")
    Optional<OneToOne> findById(String s);

}
