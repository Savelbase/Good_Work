package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.user.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.query.model.Role;
import com.rm.toolkit.user.query.model.RoleMinimalInfo;
import com.rm.toolkit.user.query.security.jwt.JwtUtil;
import com.rm.toolkit.user.query.service.RoleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Получение информации о ролях")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleQueryController {

    private final JwtUtil jwtUtil;
    private final RoleQueryService roleQueryService;

    @Operation(summary = "Получение списка всех ролей в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи список", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = RoleMinimalInfo.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).VIEW_ROLES, " +
            "T(com.rm.toolkit.user.query.security.AuthorityType).EDIT_ROLES)")
    public List<RoleMinimalInfo> getAllRoles(
            @Parameter(description = "Вывести только те роли, пользователей с которыми ты можешь создать")
            @RequestParam(required = false) boolean availableOnly,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("Вызван GET /api/v1/roles");

        if (!availableOnly) {
            return roleQueryService.getAllRoles();
        } else {
            int priority = jwtUtil.getRolePriority(jwtUtil.getTokenFromHeader(authorizationHeader));
            return roleQueryService.getAvailableForCreationRoles(priority);
        }
    }

    @Operation(summary = "Получить полную информацию о роли")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = Role.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Роли с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"ROLE_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).VIEW_ROLES, " +
            "T(com.rm.toolkit.user.query.security.AuthorityType).EDIT_ROLES)")
    public Role getRoleFullInfo(@PathVariable String roleId) {
        log.info("Вызван GET /api/v1/roles/{}", roleId);

        return roleQueryService.getRoleFullInfo(roleId);
    }
}
