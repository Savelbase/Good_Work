package com.rm.toolkit.user.query.repository;

import com.rm.toolkit.user.query.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

    @Override
    @NonNull
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT a FROM Activity a")
    List<Activity> findAll();
}
