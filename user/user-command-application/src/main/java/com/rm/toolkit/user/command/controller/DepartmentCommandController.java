package com.rm.toolkit.user.command.controller;

import com.rm.toolkit.user.command.dto.command.department.CreateDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.department.EditDepartmentCommand;
import com.rm.toolkit.user.command.dto.response.SuccessResponse;
import com.rm.toolkit.user.command.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.ConflictErrorResponse;
import com.rm.toolkit.user.command.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.command.security.SecurityUtil;
import com.rm.toolkit.user.command.service.DepartmentCommandService;
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

@Tag(name = "Работа с отделами")
@RestController
@RequestMapping("/api/v1/departments")
@Slf4j
@RequiredArgsConstructor
public class DepartmentCommandController {

    private final DepartmentCommandService departmentCommandService;

    @Operation(summary = "Создать отдел")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отдел успешно создан",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле name",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: Test333333333333333333333333333\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "409", description = "Отдел с таким именем уже существует",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"DEPARTMENT_ALREADY_EXISTS\"\n" +
                                    "}")))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_DEPARTMENTS)")
    public SuccessResponse createDepartment(@Valid @RequestBody CreateDepartmentCommand command) {
        log.debug("Вызван POST /api/v1/departments | command={}", command);

        String departmentId = departmentCommandService.createDepartment(command, SecurityUtil.getCurrentUserId());

        log.info("Пользователь {} успешно создал отдел {}", SecurityUtil.getCurrentUserId(), departmentId);
        return new SuccessResponse(departmentId);
    }

    @Operation(summary = "Редактировать отдел")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отдел успешно отредактирован",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"message\": \"cf8a4b92-8c2c-40bb-8403-cfa0964e7c47\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле name",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: Test333333333333333333333333333\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Отдел с таким id не найден",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Отдел с таким именем уже существует",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"DEPARTMENT_ALREADY_EXISTS\"\n" +
                                    "}")))
    })
    @PutMapping("/{departmentId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_DEPARTMENTS)")
    public SuccessResponse editDepartment(@PathVariable String departmentId, @Valid @RequestBody EditDepartmentCommand command) {
        log.debug("Вызван PUT /api/v1/departments/{} | command={}", departmentId, command);

        departmentCommandService.editDepartment(departmentId, command, SecurityUtil.getCurrentUserId());

        log.info("Пользователь {} успешно отредактировал отдел {}", SecurityUtil.getCurrentUserId(), departmentId);
        return SuccessResponse.getGeneric();
    }

    @Operation(summary = "Удалить отдел")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отдел успешно удалён",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Отдел с таким id не найден",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.user.command.security.AuthorityType).EDIT_DEPARTMENTS)")
    public SuccessResponse deleteDepartment(@PathVariable String departmentId) {
        log.debug("Вызван DELETE /api/v1/departments/{}", departmentId);

        departmentCommandService.deleteDepartment(departmentId, SecurityUtil.getCurrentUserId());

        log.info("Пользователь {} успешно удалил отдел {}", SecurityUtil.getCurrentUserId(), departmentId);
        return SuccessResponse.getGeneric();
    }
}
