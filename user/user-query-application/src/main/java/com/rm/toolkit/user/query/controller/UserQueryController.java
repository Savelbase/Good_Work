package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.dto.query.UserQuery;
import com.rm.toolkit.user.query.dto.response.BooleanResponse;
import com.rm.toolkit.user.query.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.user.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.model.UserMediumInfo;
import com.rm.toolkit.user.query.model.UserMinimalInfo;
import com.rm.toolkit.user.query.security.AuthorityType;
import com.rm.toolkit.user.query.security.SecurityUtil;
import com.rm.toolkit.user.query.service.UserQueryService;
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
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Получение информации о пользователях")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserQueryController {

    private final UserQueryService userQueryService;

    @Operation(summary = "Получить список всех пользователей с пагинацией и фильтрами",
            description = "Страницы начинаются с нуля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи список", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = UserMediumInfo.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия. Фильтровать по статусу могут только Admin и RD", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
    })
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).EMPLOYEE_LIST)")
    public Page<UserMediumInfo> getListOfUsers(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "50") int size,
                                               @ParameterObject @ModelAttribute UserQuery userQuery) {
        log.info("Вызван GET /api/v1/users | page={} size={} userQuery={}", page, size, userQuery);


        if (userQuery.getStatus() != null
                && !SecurityUtil.getCurrentUserAuthorities().contains(AuthorityType.USER_STATUS_SETTINGS)) {
            log.info("Пользователь с id {} попытался отфильтровать список пользователей по статусу",
                    SecurityUtil.getCurrentUserId());
            throw new AccessDeniedException("У вас нет прав для фильтра по статусу");
        }

        return userQueryService.getUsers(page, size, userQuery);
    }

    @Operation(summary = "Получить список всех пользователей с пагинацией и фильтрами. Minimal edition.",
            description = "Страницы начинаются с нуля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи список", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = UserMinimalInfo.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия. Фильтровать по статусу могут только Admin и RD", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
    })
    @GetMapping("/minimal")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).EMPLOYEE_LIST)")
    public Page<UserMinimalInfo> getListOfUsersMinimal(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "50") int size,
                                                       @ParameterObject @ModelAttribute UserQuery userQuery) {
        log.info("Вызван GET /api/v1/users/minimal | page={} size={} userQuery={}", page, size, userQuery);

        if (userQuery.getStatus() != null
                && !SecurityUtil.getCurrentUserAuthorities().contains(AuthorityType.USER_STATUS_SETTINGS)) {
            log.info("Пользователь с id {} попытался отфильтровать список пользователей по статусу",
                    SecurityUtil.getCurrentUserId());
            throw new AccessDeniedException("У вас нет прав для фильтра по статусу");
        }

        return userQueryService.getUsersMinimal(page, size, userQuery);
    }

    @Operation(summary = "Получить подробную информацию о пользователе по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(
                    implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).EMPLOYEE_LIST)")
    public User getUserFullInfo(@PathVariable String userId) {
        log.info("Вызван GET /api/v1/users/{}", userId);

        return userQueryService.getUser(userId);
    }

    @Operation(summary = "Находится ли пользователь в моём пуле подчинённых")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(
                    implementation = User.class
            ))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping(value = "/{userId}/isInMyPool")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).EMPLOYEE_LIST)")
    public BooleanResponse isUserInMyPool(@PathVariable String userId) {
        log.info("Вызван GET /api/v1/users/{}/isInMyPool", userId);

        return new BooleanResponse(userQueryService.isUserInPool(userId, SecurityUtil.getCurrentUserId()));
    }
}
