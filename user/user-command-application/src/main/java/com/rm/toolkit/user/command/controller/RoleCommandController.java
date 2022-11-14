package com.rm.toolkit.user.command.controller;

import com.rm.toolkit.user.command.dto.command.role.CreateRoleCommand;
import com.rm.toolkit.user.command.dto.command.role.EditRoleCommand;
import com.rm.toolkit.user.command.dto.response.SuccessResponse;
import com.rm.toolkit.user.command.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.ConflictErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.UnprocessableEntityErrorResponse;
import com.rm.toolkit.user.command.security.SecurityUtil;
import com.rm.toolkit.user.command.service.RoleCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Работа с ролями")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleCommandController {

    private final RoleCommandService roleCommandService;

    @Operation(summary = "Создать новую роль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Роль успешно создана",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"message\": \"cf8a4b92-8c2c-40bb-8403-cfa0964e7c47\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Поле authorities заполнено неправильно",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле name",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: Мимокро666666666кодил\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "409", description = "Роль с таким именем уже существует", content = @Content(
                    schema = @Schema(implementation = ConflictErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Приоритет вышел за пределы или попытка создать" +
                    " роль с админискими правами (EDIT_DEPARTMENT, EDIT_ROLE, EDIT_INTERVALS)", content = @Content(schema = @Schema(
                    implementation = UnprocessableEntityErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_ROLES)")
    public SuccessResponse createRole(@Valid @RequestBody CreateRoleCommand command) {
        log.debug("Вызван POST /api/v1/roles | command={}", command);

        String roleId = roleCommandService.createRole(command, SecurityUtil.getCurrentUserId());

        log.info("Пользователь {} успешно создал роль {}", SecurityUtil.getCurrentUserId(), roleId);
        return new SuccessResponse(roleId);
    }

    @Operation(summary = "Редактировать роль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно отредактирована", content = @Content(
                    schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Поле authorities заполнено неправильно",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле name",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: Мимокро666666666кодил\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Роль с таким id не найдена", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Роль с таким именем уже существует", content = @Content(
                    schema = @Schema(implementation = ConflictErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Нельзя убрать USER_STATUS_SETTINGS и " +
                    "ADD_EMPLOYEE_TO_DEPARTMENT у роли, если есть хоть один имеющий пользователь, " +
                    "который является главой отдела", content = @Content(
                    schema = @Schema(implementation = ConflictErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_HAS_DEPARTMENTS\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "422", description = "Приоритет вышел за пределы или попытка создать" +
                    " роль с админискими правами (EDIT_DEPARTMENT, EDIT_ROLE, EDIT_INTERVALS), " +
                    "или попытка редактировать базовую роль", content = @Content(schema = @Schema(
                    implementation = UnprocessableEntityErrorResponse.class)))
    })
    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_ROLES)")
    public SuccessResponse editRole(@PathVariable String roleId, @Valid @RequestBody EditRoleCommand command) {
        log.debug("Вызван PUT /api/v1/roles/{} | command={}", roleId, command);

        roleCommandService.editRole(roleId, command, SecurityUtil.getCurrentUserId());

        log.info("Пользователь {} успешно отредактировал роль {}", SecurityUtil.getCurrentUserId(), roleId);
        return SuccessResponse.getGeneric();
    }

    @Operation(summary = "Удалить роль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно удалена", content = @Content(
                    schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Роль с таким id не найдена", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Нельзя удалить базовые роли",
                    content = @Content(schema = @Schema(implementation = UnprocessableEntityErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"BASE_ROLE_NOT_DELETABLE\"\n" +
                                    "}"))),
    })
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_ROLES)")
    public SuccessResponse deleteRole(@PathVariable String roleId) {
        log.debug("Вызван DELETE /api/v1/roles/{}", roleId);

        roleCommandService.deleteRole(roleId, SecurityUtil.getCurrentUserId());

        log.info("Пользователь {} успешно удалил роль {}", SecurityUtil.getCurrentUserId(), roleId);
        return SuccessResponse.getGeneric();
    }
}
