package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.security.SecurityUtil;
import com.rm.toolkit.user.query.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Получение информации о себе")
@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
@Slf4j
public class MeQueryController {

    private final UserQueryService userQueryService;

    @Operation(summary = "Получить подробную информацию о себе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).AUTHORIZATION)")
    public User getUserInfo() {
        String id = SecurityUtil.getCurrentUserId();

        log.info("Вызван GET /api/v1/me | id={}", id);

        return userQueryService.getUser(id);
    }
}
