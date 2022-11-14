package com.rm.toolkit.oneToOne.command.repository;

import com.rm.toolkit.oneToOne.command.model.OneToOne;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneToOneRepository extends PagingAndSortingRepository<OneToOne, String>,
        JpaSpecificationExecutor<OneToOne> {

    @Override
    @Query("SELECT o FROM OneToOne o WHERE o.isDeleted = false")
    Iterable<OneToOne> findAll();

    @Override
    @Query("SELECT o FROM OneToOne o WHERE o.id = ?1 AND o.isDeleted = false")
    Optional<OneToOne> findById(String s);

    Optional<OneToOne> findByIdAndResourceManagerIdAndIsDeletedIsFalse(String id, String rmId);

}
