package com.rm.toolkit.user.command.controller;

import com.google.gson.Gson;
import com.rm.toolkit.user.command.advice.GlobalExceptionHandler;
import com.rm.toolkit.user.command.dto.command.user.ChangeUserDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.user.ChangeUserStatusCommand;
import com.rm.toolkit.user.command.dto.command.user.CreateUserCommand;
import com.rm.toolkit.user.command.dto.command.user.EditUserCommand;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.security.SecurityConfig;
import com.rm.toolkit.user.command.security.SecurityUtil;
import com.rm.toolkit.user.command.security.jwt.JwtUserDetailsService;
import com.rm.toolkit.user.command.security.jwt.JwtUtil;
import com.rm.toolkit.user.command.service.UserCommandService;
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
import java.util.Set;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWebMvc
@SpringBootTest(classes = {
        UserCommandController.class,
        GlobalExceptionHandler.class,
        SecurityUtil.class,
        SecurityConfig.class,
        JwtUtil.class,
        JwtUserDetailsService.class,
        TestTokenBuilder.class
},
        properties = "spring.cloud.config.enabled=false")
class UserCommandControllerTest {

    @MockBean
    private UserCommandService service;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private TestTokenBuilder tokenBuilder;

    @Autowired
    private MockMvc mvc;

    private final String authorId = "user";
    private final String userId = "Test";

    int rolePriority = 0;

    private final Supplier<CreateUserCommand> createUserCommandSupplier = () -> {
        CreateUserCommand command = new CreateUserCommand();
        command.setFirstName("Test");
        command.setLastName("Test");
        command.setEmail("Test");
        command.setRoleId("Test");
        command.setDepartmentId("Test");
        command.setResourceManagerId("Test");
        command.setCityId("Test");
        command.setAvatarPath("Test");
        command.setActivitiesIds(new HashSet<>());
        return command;
    };

    private final Supplier<EditUserCommand> editUserCommandSupplier = () -> {
        EditUserCommand command = new EditUserCommand();
        command.setFirstName("Test");
        command.setLastName("Test");
        command.setEmail("Test");
        command.setRoleId("Test");
        command.setDepartmentId("Test");
        command.setResourceManagerId("Test");
        command.setCityId("Test");
        command.setAvatarPath("Test");
        command.setActivitiesIds(new HashSet<>());
        return command;
    };

    private final Supplier<ChangeUserDepartmentCommand> changeUserDepartmentCommandSupplier = () -> {
        ChangeUserDepartmentCommand command = new ChangeUserDepartmentCommand();
        command.setDepartmentId("Test");
        return command;
    };

    private final Supplier<ChangeUserStatusCommand> changeUserStatusCommandSupplier = () -> {
        ChangeUserStatusCommand command = new ChangeUserStatusCommand();
        command.setStatus(StatusType.ACTIVE);
        return command;
    };

    @Test
    void createUserWithoutAuthorization() throws Exception {
        CreateUserCommand command = createUserCommandSupplier.get();
        String json = convertUserCommandToJson(command);

        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE_CARD")
    void createUserWithAuthority() throws Exception {
        CreateUserCommand command = createUserCommandSupplier.get();
        String json = convertUserCommandToJson(command);
        Set<AuthorityType> authorities = new HashSet<>();
        authorities.add(AuthorityType.EMPLOYEE_CARD);

        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(201));
        Mockito.verify(service).createUser(command, authorId, rolePriority, authorities);
    }

    @Test
    void editUserWithoutAuthorization() throws Exception {
        EditUserCommand command = editUserCommandSupplier.get();
        String json = convertUserCommandToJson(command);

        mvc.perform(put("/api/v1/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE_CARD")
    void editUserWithAuthority() throws Exception {
        EditUserCommand command = editUserCommandSupplier.get();
        String json = convertUserCommandToJson(command);
        Set<AuthorityType> authorities = new HashSet<>();
        authorities.add(AuthorityType.EMPLOYEE_CARD);

        mvc.perform(put("/api/v1/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(200));
        Mockito.verify(service).editUser(userId, command, authorId, rolePriority, authorities);
    }

    @Test
    void changeUserDepartmentWithoutAuthorization() throws Exception {
        ChangeUserDepartmentCommand command = changeUserDepartmentCommandSupplier.get();
        String json = convertUserCommandToJson(command);

        mvc.perform(patch("/api/v1/users/" + userId + "/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "ADD_EMPLOYEE_TO_DEPARTMENT")
    void changeUserDepartmentWithAuthority() throws Exception {
        ChangeUserDepartmentCommand command = changeUserDepartmentCommandSupplier.get();
        String json = convertUserCommandToJson(command);
        Set<AuthorityType> authorities = new HashSet<>();
        authorities.add(AuthorityType.ADD_EMPLOYEE_TO_DEPARTMENT);

        mvc.perform(patch("/api/v1/users/" + userId + "/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(200));
        Mockito.verify(service).changeUserDepartment(userId, command, authorId, authorities);
    }

    @Test
    void changeUserStatusWithoutAuthorization() throws Exception {
        ChangeUserStatusCommand command = changeUserStatusCommandSupplier.get();
        String json = convertUserCommandToJson(command);

        mvc.perform(patch("/api/v1/users/" + userId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(authorities = "USER_STATUS_SETTINGS")
    void changeUserStatusWithAuthority() throws Exception {
        ChangeUserStatusCommand command = changeUserStatusCommandSupplier.get();
        String json = convertUserCommandToJson(command);
        Set<AuthorityType> authorities = new HashSet<>();
        authorities.add(AuthorityType.USER_STATUS_SETTINGS);

        mvc.perform(patch("/api/v1/users/" + userId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(200));
        Mockito.verify(service).changeUserStatus(userId, command.getStatus(), authorId, authorities);
    }

    private <T> String convertUserCommandToJson(T command) {
        Gson gson = new Gson();
        return gson.toJson(command);
    }
}