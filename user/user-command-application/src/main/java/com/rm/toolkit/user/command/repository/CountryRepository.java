package com.rm.toolkit.user.command.repository;

import com.rm.toolkit.user.command.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    @Override
    @NonNull
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT c FROM Country c WHERE c.id=:id")
    Optional<Country> findById(@Param("id") @NonNull String id);

    @Override
    @NonNull
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT c FROM Country c")
    List<Country> findAll();
}
