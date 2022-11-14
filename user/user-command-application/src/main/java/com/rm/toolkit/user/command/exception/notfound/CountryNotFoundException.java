package com.rm.toolkit.user.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CountryNotFoundException extends ResponseStatusException {
    public CountryNotFoundException(String countryName, boolean isName) {
        super(HttpStatus.NOT_FOUND, String.format("В стране %s нет офисов этой компании", countryName));
    }

    public CountryNotFoundException(String countryId) {
        super(HttpStatus.NOT_FOUND, String.format("В стране с id %s нет офисов этой компании", countryId));
    }
}
