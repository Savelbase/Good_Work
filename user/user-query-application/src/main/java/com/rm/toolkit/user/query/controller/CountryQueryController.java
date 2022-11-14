package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.query.model.City;
import com.rm.toolkit.user.query.model.Country;
import com.rm.toolkit.user.query.service.CountryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Tag(name = "Получение информации о странах")
@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@Slf4j
public class CountryQueryController {

    private final CountryQueryService countryQueryService;

    @Operation(summary = "Получить список всех стран")
    @GetMapping
    public List<Country> getAllCountries() {
        log.info("Вызван GET /api/v1/countries");

        return countryQueryService.getAllCountries();
    }

    @Operation(summary = "Получить список всех городов в стране с заданным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = City.class)))),
            @ApiResponse(responseCode = "404", description = "Страна с таким id не найдена", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"COUNTRY_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping("/{countryId}/cities")
    public Set<City> getCitiesForCountry(@PathVariable String countryId) {
        log.info("Вызван GET /api/v1/countries/{}/cities", countryId);

        return countryQueryService.getCitiesForCountry(countryId);
    }
}
