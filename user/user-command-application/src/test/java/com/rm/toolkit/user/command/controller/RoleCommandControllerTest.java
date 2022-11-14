package com.rm.toolkit.user.command.controller;

import com.google.gson.Gson;
import com.rm.toolkit.user.command.advice.GlobalExceptionHandler;
import com.rm.toolkit.user.command.dto.command.role.CreateRoleCommand;
import com.rm.toolkit.user.command.dto.command.role.EditRoleCommand;
import com.rm.toolkit.user.command.security.SecurityConfig;
import com.rm.toolkit.user.command.security.SecurityUtil;
import com.rm.toolkit.user.command.security.jwt.JwtUserDetailsService;
import com.rm.toolkit.user.command.security.jwt.JwtUtil;
import com.rm.toolkit.user.command.service.RoleCommandService;
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
        RoleCommandController.class,
        GlobalExceptionHandler.class,
        SecurityUtil.class,
        SecurityConfig.class,
        JwtUtil.class,
        JwtUserDetailsService.class,
        TestTokenBuilder.class
},
        properties = "spring.cloud.config.enabled=false")
class RoleCommandControllerTest {
    @Autowired
    private TestTokenBuilder tokenBuilder;

    @MockBean
    private RoleCommandService service;

    @Autowired
    private MockMvc mvc;

    private final String authorId = "user";
    private final String roleId = "Test";

    private final Supplier<CreateRoleCommand> createRoleCommandSupplier = () -> {
        CreateRoleCommand command = new CreateRoleCommand();
        command.setName("Test");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        return command;
    };

    private final Supplier<EditRoleCommand> editRoleCommandSupplier = () -> {
        EditRoleCommand command = new EditRoleCommand();
        command.setName("Test");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        return command;
    };


    @Test
    void createRoleWithoutAuthorization() throws Exception {
        CreateRoleCommand command = createRoleCommandSupplier.get();
        String json = convertRoleCommandToJson(command);

        mvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EDIT_ROLES")
    void createRoleWithAuthority() throws Exception {
        CreateRoleCommand command = createRoleCommandSupplier.get();
        String json = convertRoleCommandToJson(command);

        mvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(201));
        Mockito.verify(service).createRole(command, authorId);
    }

    @Test
    void editRoleWithoutAuthorization() throws Exception {
        EditRoleCommand command = editRoleCommandSupplier.get();
        String json = convertRoleCommandToJson(command);

        mvc.perform(put("/api/v1/roles/" + roleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EDIT_ROLES")
    void editRoleWithAuthority() throws Exception {
        EditRoleCommand command = editRoleCommandSupplier.get();
        String json = convertRoleCommandToJson(command);

        mvc.perform(put("/api/v1/roles/" + roleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(200));
        Mockito.verify(service).editRole(roleId, command, authorId);
    }

    @Test
    void deleteRoleWithoutAuthorization() throws Exception {
        mvc.perform(delete("/api/v1/roles/" + roleId))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EDIT_ROLES")
    void deleteRoleWithAuthority() throws Exception {

        mvc.perform(delete("/api/v1/roles/" + roleId)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(200));
        Mockito.verify(service).deleteRole(roleId, authorId);
    }

    private <T> String convertRoleCommandToJson(T command) {
        Gson gson = new Gson();
        return gson.toJson(command);
    }
}