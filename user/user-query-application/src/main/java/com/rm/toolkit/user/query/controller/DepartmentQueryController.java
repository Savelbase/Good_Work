package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.user.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.user.query.model.Department;
import com.rm.toolkit.user.query.model.DepartmentMinimalInfo;
import com.rm.toolkit.user.query.model.UserMediumInfo;
import com.rm.toolkit.user.query.model.UserMinimalInfo;
import com.rm.toolkit.user.query.service.DepartmentQueryService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Tag(name = "Получение списка отделов")
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentQueryController {

    private final DepartmentQueryService departmentQueryService;
    private final UserQueryService userQueryService;

    @Operation(summary = "Получить список всех отделов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи список", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = DepartmentMinimalInfo.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).VIEW_DEPARTMENTS, " +
            "T(com.rm.toolkit.user.query.security.AuthorityType).EDIT_DEPARTMENTS)")
    public List<DepartmentMinimalInfo> getAllDepartments() {
        log.info("Вызван GET /api/v1/departments");

        return departmentQueryService.getAllDepartments();
    }

    @Operation(summary = "Получить подробную информацию об отделе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи подробную информацию", content = @Content(schema = @Schema(
                    implementation = Department.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Отдел с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"DEPARTMENT_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping("/{departmentId}")
    @PreAuthorize("hasAnyAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).VIEW_DEPARTMENTS, " +
            "T(com.rm.toolkit.user.query.security.AuthorityType).EDIT_DEPARTMENTS)")
    public Department getDepartmentInfo(@PathVariable String departmentId) {
        return departmentQueryService.getDepartmentFullInfo(departmentId);
    }

    @Operation(summary = "Получить список пользователей в отделе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи список", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = UserMediumInfo.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class)))
    })
    @GetMapping("/{departmentId}/users")
    @PreAuthorize("hasAnyAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).VIEW_DEPARTMENTS, " +
            "T(com.rm.toolkit.user.query.security.AuthorityType).EDIT_DEPARTMENTS)")
    public Set<UserMinimalInfo> getDepartmentMembers(@PathVariable String departmentId) {
        return userQueryService.getUsersMinimalInfoByDepartment(departmentId);
    }

    @Operation(summary = "Получить инфу о отделе 'без отдела'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи подробную информацию", content = @Content(schema = @Schema(
                    implementation = Department.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
    })
    @GetMapping("/no_department")
    @PreAuthorize("hasAnyAuthority(T(com.rm.toolkit.user.query.security.AuthorityType).VIEW_DEPARTMENTS, " +
            "T(com.rm.toolkit.user.query.security.AuthorityType).EDIT_DEPARTMENTS)")
    public Department getNoDepartment() {
        return departmentQueryService.getNoDepartmentId();
    }
}
