package com.rm.toolkit.user.query.service;

import com.rm.toolkit.user.query.model.City;
import com.rm.toolkit.user.query.model.Country;
import com.rm.toolkit.user.query.repository.CityRepository;
import com.rm.toolkit.user.query.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryQueryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Set<City> getCitiesForCountry(String countryId) {
        return cityRepository.findAllByCountryId(countryId);
    }
}
