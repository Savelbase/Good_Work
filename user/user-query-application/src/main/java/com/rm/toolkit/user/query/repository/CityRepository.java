package com.rm.toolkit.user.query.repository;

import com.rm.toolkit.user.query.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Set;

@Repository
public interface CityRepository extends JpaRepository<City, String> {

    @Override
    @NonNull
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT c FROM City c")
    List<City> findAll();

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT c FROM City c WHERE c.country.id=:countryId")
    Set<City> findAllByCountryId(@Param("countryId") String countryId);
}
