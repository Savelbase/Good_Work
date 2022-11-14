package com.rm.toolkit.user.command.controller;

import com.google.gson.Gson;
import com.rm.toolkit.user.command.advice.GlobalExceptionHandler;
import com.rm.toolkit.user.command.dto.command.department.CreateDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.department.EditDepartmentCommand;
import com.rm.toolkit.user.command.security.SecurityConfig;
import com.rm.toolkit.user.command.security.SecurityUtil;
import com.rm.toolkit.user.command.security.jwt.JwtUserDetailsService;
import com.rm.toolkit.user.command.security.jwt.JwtUtil;
import com.rm.toolkit.user.command.service.DepartmentCommandService;
import com.rm.toolkit.user.command.testUtil.TestTokenBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWebMvc
@SpringBootTest(classes = {
        DepartmentCommandController.class,
        GlobalExceptionHandler.class,
        SecurityUtil.class,
        SecurityConfig.class,
        JwtUtil.class,
        JwtUserDetailsService.class,
        TestTokenBuilder.class
},
        properties = "spring.cloud.config.enabled=false")
class DepartmentCommandControllerTest {
    @Autowired
    private TestTokenBuilder tokenBuilder;

    @MockBean
    private DepartmentCommandService service;

    @Autowired
    private MockMvc mvc;

    private final String authorId = "user";
    private final String departmentId = "Test";

    private final Supplier<CreateDepartmentCommand> createDepartmentCommandSupplier = () -> {
        CreateDepartmentCommand command = new CreateDepartmentCommand();
        command.setName("Test");
        command.setHeadId("Test");
        command.setMembersIds(new HashSet<>());
        return command;
    };

    private final Supplier<EditDepartmentCommand> editDepartmentCommandSupplier = () -> {
        EditDepartmentCommand command = new EditDepartmentCommand();
        command.setName("Test");
        command.setHeadId("Test");
        return command;
    };

    @Test
    void createDepartmentWithoutAuthorization() throws Exception {
        CreateDepartmentCommand command = createDepartmentCommandSupplier.get();
        String json = commandToString(command);

        mvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EDIT_DEPARTMENTS")
    void createDepartmentWithAuthority() throws Exception {
        CreateDepartmentCommand command = createDepartmentCommandSupplier.get();
        String json = commandToString(command);

        mvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(201));
        Mockito.verify(service).createDepartment(command, authorId);
    }

    @Test
    void editDepartmentWithoutAuthorization() throws Exception {
        EditDepartmentCommand command = editDepartmentCommandSupplier.get();
        String json = commandToString(command);

        mvc.perform(put("/api/v1/departments/" + departmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EDIT_DEPARTMENTS")
    void editDepartmentWithAuthority() throws Exception {
        EditDepartmentCommand command = editDepartmentCommandSupplier.get();
        String json = commandToString(command);

        mvc.perform(put("/api/v1/departments/" + departmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(200));
        Mockito.verify(service).editDepartment(departmentId, command, authorId);
    }

    @Test
    void deleteDepartmentWithoutAuthorization() throws Exception {
        mvc.perform(delete("/api/v1/departments/" + departmentId))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EDIT_DEPARTMENTS")
    void deleteDepartmentWithAuthority() throws Exception {
        mvc.perform(delete("/api/v1/departments/" + departmentId))
                .andExpect(status().is(200));
        Mockito.verify(service).deleteDepartment(departmentId, authorId);
    }

    private <T> String commandToString(T command) {
        Gson gson = new Gson();
        return gson.toJson(command);
    }
}