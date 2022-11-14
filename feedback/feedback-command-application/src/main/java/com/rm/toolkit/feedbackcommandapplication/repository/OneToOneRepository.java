package com.rm.toolkit.feedbackcommandapplication.repository;

import com.rm.toolkit.feedbackcommandapplication.model.OneToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OneToOneRepository extends JpaRepository<OneToOne, String> {

    @Query("select o from OneToOne o where o.id=:oneToOneId and o.isOver = true and o.isDeleted = false")
    Optional<OneToOne> findCompletedOneToOneById(@Param("oneToOneId") String oneToOneId);

    @Query("select o from OneToOne o where o.isOver = true and o.isDeleted = false")
    List<OneToOne> findAllCompletedOneToOne();
}
