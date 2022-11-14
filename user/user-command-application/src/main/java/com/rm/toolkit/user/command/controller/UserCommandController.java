package com.rm.toolkit.user.command.controller;

import com.rm.toolkit.user.command.dto.command.user.*;
import com.rm.toolkit.user.command.dto.response.SuccessResponse;
import com.rm.toolkit.user.command.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.ConflictErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.command.security.SecurityUtil;
import com.rm.toolkit.user.command.security.jwt.JwtUtil;
import com.rm.toolkit.user.command.service.UserCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Работа с пользователями")
public class UserCommandController {

    private final UserCommandService userCommandService;

    private final JwtUtil jwtUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EMPLOYEE_CARD)")
    @Operation(summary = "Создать нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"message\": \"cf8a4b92-8c2c-40bb-8403-cfa0964e7c47\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле email",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: js33333333333333333333333tw@hap.work\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Активность, роль, город или отдел с таким id не найдены",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"USER_ALREADY_EXISTS_BY_NAME\"\n" +
                                    "}")))
    })
    public SuccessResponse createUser(@Valid @RequestBody CreateUserCommand command,
                                      @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String authorizationHeader) {
        log.debug("Вызван POST /api/v1/users | command = {}", command);

        String createdUserId = userCommandService.createUser(command, SecurityUtil.getCurrentUserId(),
                jwtUtil.getRolePriorityFromHeader(authorizationHeader), SecurityUtil.getCurrentUserAuthorities());

        log.info("Пользователь {} успешно создал пользователя {}", SecurityUtil.getCurrentUserId(), createdUserId);
        return new SuccessResponse(createdUserId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EMPLOYEE_CARD)")
    @Operation(summary = "Изменить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно отредактирован",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле email",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: js33333333333333333333333tw@hap.work\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь, активность, роль, город или отдел с таким " +
                    "id не найдены", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"USER_ALREADY_EXISTS_BY_EMAIL\"\n" +
                                    "}")))
    })
    public SuccessResponse editUser(@PathVariable String userId, @Valid @RequestBody EditUserCommand command,
                                    @RequestHeader(value = "Authorization", required = false) @Parameter(hidden = true) String authorizationHeader) {
        log.debug("Вызван PUT /api/v1/users/{} | command={}", userId, command);

        userCommandService.editUser(userId, command, SecurityUtil.getCurrentUserId(),
                jwtUtil.getRolePriorityFromHeader(authorizationHeader), SecurityUtil.getCurrentUserAuthorities());

        log.info("Пользователь {} успешно отредактировал пользователя {}", SecurityUtil.getCurrentUserId(), userId);
        return SuccessResponse.getGeneric();
    }

    @PatchMapping("/{userId}/department")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).ADD_EMPLOYEE_TO_DEPARTMENT)")
    @Operation(summary = "Изменить отдел пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отдел успешно изменён",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь или отдел с таким id не найден",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Нельзя переносить в другой отделов юзеров, которые " +
                    "являются главами отделов",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"SRM_HAS_DEPARTMENTS\"\n" +
                                    "}")))
    })
    public SuccessResponse changeUserDepartment(@PathVariable String userId,
                                                @Valid @RequestBody ChangeUserDepartmentCommand command) {
        log.info("Вызван PATCH /api/v1/users/{}/department | command={}", userId, command);

        userCommandService.changeUserDepartment(userId, command, SecurityUtil.getCurrentUserId(),
                SecurityUtil.getCurrentUserAuthorities());
        return SuccessResponse.getGeneric();
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).USER_STATUS_SETTINGS)")
    @Operation(summary = "Изменить статус пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно изменён",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Статус указан неверно",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    public SuccessResponse changeUserStatus(@PathVariable String userId,
                                            @Valid @RequestBody ChangeUserStatusCommand command) {
        log.debug("Вызван PATCH /api/v1/users/{}/status | command={}", userId, command);

        userCommandService.changeUserStatus(userId, command.getStatus(), SecurityUtil.getCurrentUserId(),
                SecurityUtil.getCurrentUserAuthorities());

        log.info("Пользователь {} успешно изменил статус пользователя {}", SecurityUtil.getCurrentUserId(), userId);
        return SuccessResponse.getGeneric();
    }

    @PatchMapping("/{departmentId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_DEPARTMENTS)")
    @Operation(summary = "Изменить отдел у пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "У пользователей, отдел успешно изменен",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь или отдел с таким id не найден",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Нельзя переносить в другой отделов юзеров, которые " +
                    "являются главами отделов",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"SRM_HAS_DEPARTMENTS\"\n" +
                                    "}")))
    })
    public SuccessResponse changeUsersDepartment(@PathVariable String departmentId, @Valid @RequestBody ChangeUsersDepartmentCommand command) {
        log.debug("Вызван PATCH /api/v1/users/{departmentId}| command={}", SecurityUtil.getCurrentUserId());

        userCommandService.changeUsersDepartment(departmentId, command, SecurityUtil.getCurrentUserId(),
                SecurityUtil.getCurrentUserAuthorities());

        log.info("Пользователь {} успешно изменил пользователям отдел {}", SecurityUtil.getCurrentUserId(), command);
        return SuccessResponse.getGeneric();
    }
}
