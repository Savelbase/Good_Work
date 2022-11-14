package com.rm.toolkit.user.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CityNotFoundException extends ResponseStatusException {
    public CityNotFoundException(String cityName, boolean isName) {
        super(HttpStatus.NOT_FOUND, String.format("В городе %s нет офисов этой компании", cityName));
    }

    public CityNotFoundException(String cityId) {
        super(HttpStatus.NOT_FOUND, String.format("В городе с id %s нет офисов этой компании", cityId));
    }
}
