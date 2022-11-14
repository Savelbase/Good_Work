package com.rm.toolkit.comment.command.controller;

import com.google.gson.Gson;
import com.rm.toolkit.comment.command.advise.GlobalExceptionHandler;
import com.rm.toolkit.comment.command.dto.command.CreateCommentCommand;
import com.rm.toolkit.comment.command.dto.command.EditCommentCommand;
import com.rm.toolkit.comment.command.security.SecurityConfig;
import com.rm.toolkit.comment.command.security.SecurityUtil;
import com.rm.toolkit.comment.command.security.jwt.CustomUserDetailsService;
import com.rm.toolkit.comment.command.security.jwt.JwtUtil;
import com.rm.toolkit.comment.command.service.CommentCommandService;
import com.rm.toolkit.comment.command.testUtil.TestTokenBuilder;
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

import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWebMvc
@SpringBootTest(classes = {
        GlobalExceptionHandler.class,
        SecurityUtil.class,
        SecurityConfig.class,
        JwtUtil.class,
        CustomUserDetailsService.class,
        TestTokenBuilder.class,
        CommentCommandController.class
},
        properties = "spring.cloud.config.enabled=false")
class CommentCommandControllerTest {
    @MockBean
    private CommentCommandService service;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    private TestTokenBuilder tokenBuilder;
    @Autowired
    private MockMvc mvc;
    private final String authorId = "user";

    private final Supplier<CreateCommentCommand> createCommentCommandSupplier = () -> {
        CreateCommentCommand command = new CreateCommentCommand();
        command.setId("Test");
        command.setUserId("Test");
        command.setSenderId("Test");
        command.setText("Test");
        return command;
    };

    private final Supplier<EditCommentCommand> editCommentCommandSupplier = () -> {
        EditCommentCommand command = new EditCommentCommand();
        command.setId("Test");
        command.setUserId("Test");
        command.setSenderId("Test");
        command.setText("Test");
        return command;
    };

    @Test
    void createCommentWithoutAuthorization() throws Exception {
        CreateCommentCommand command = createCommentCommandSupplier.get();
        String json = convertCommentCommandToJson(command);

        mvc.perform(post("/api/v1/comments/" + command.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    @Test
    void editUserCommentWithoutAuthorization() throws Exception {
        EditCommentCommand command = editCommentCommandSupplier.get();
        String json = convertCommentCommandToJson(command);

        mvc.perform(put("/api/v1/comments/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(403));
    }

    private <T> String convertCommentCommandToJson(T command) {
        Gson gson = new Gson();
        return gson.toJson(command);
    }
}