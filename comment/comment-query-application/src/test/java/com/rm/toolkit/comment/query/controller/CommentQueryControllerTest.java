package com.rm.toolkit.comment.query.controller;

import com.rm.toolkit.comment.query.advice.GlobalExceptionHandler;
import com.rm.toolkit.comment.query.model.Comment;
import com.rm.toolkit.comment.query.security.SecurityConfig;
import com.rm.toolkit.comment.query.security.SecurityUtil;
import com.rm.toolkit.comment.query.security.jwt.CustomUserDetailsService;
import com.rm.toolkit.comment.query.security.jwt.JwtUtil;
import com.rm.toolkit.comment.query.service.CommentQueryService;
import com.rm.toolkit.comment.query.testUtils.TestTokenBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        CommentQueryController.class
},
        properties = "spring.cloud.config.enabled=false")
public class CommentQueryControllerTest {

    private String userId = "test";
    @MockBean
    private CommentQueryService service;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private TestTokenBuilder tokenBuilder;

    @Autowired
    private MockMvc mvc;


    @Test
    void getCommentsWithoutAuthorization() throws Exception {
        mvc.perform(get("/api/v1/comments/" + userId))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(authorities = "VIEW_COMMENTS")
    void getCommentsWithAuthorization() throws Exception {
        List<Comment> comments = List.of();
        Mockito.when(service.getCommentsByUserId(anyString(), anyInt(), anyInt())).thenReturn(comments);
        mvc.perform(get("/api/v1/comments/" + userId)
                        .header("Authorization", "Bearer " + tokenBuilder.newToken.get()))
                .andExpect(status().is(200));

    }
}
