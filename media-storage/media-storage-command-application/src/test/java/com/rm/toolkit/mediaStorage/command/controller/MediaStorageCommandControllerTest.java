package com.rm.toolkit.mediaStorage.command.controller;

import com.rm.toolkit.mediaStorage.command.advice.GlobalExceptionHandler;
import com.rm.toolkit.mediaStorage.command.security.SecurityConfig;
import com.rm.toolkit.mediaStorage.command.security.SecurityUtil;
import com.rm.toolkit.mediaStorage.command.security.jwt.JwtUserDetailsService;
import com.rm.toolkit.mediaStorage.command.security.jwt.JwtUtil;
import com.rm.toolkit.mediaStorage.command.service.MediaStorageCommandService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWebMvc
@SpringBootTest(classes = {
        MediaStorageCommandController.class,
        GlobalExceptionHandler.class,
        SecurityUtil.class,
        SecurityConfig.class,
        JwtUtil.class,
        JwtUserDetailsService.class
},
        properties = "spring.cloud.config.enabled=false")
class MediaStorageCommandControllerTest {

    private final Supplier<String> newToken = () -> {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("sub", "e45bfff0-1b04-4b8a-ac95-9629dff88a3e");
        tokenData.put("iss", "rmt_mobile_auth");
        tokenData.put("exp", calendar.getTime());
        tokenData.put("rolePriority", "10");
        tokenData.put("authorities", new String[]{
                "AUTHORIZATION",
                "EMPLOYEE_LIST",
                "EMPLOYEE_CARD",
                "USER_STATUS_SETTINGS",
                "ADD_EMPLOYEE_TO_DEPARTMENT",
                "VIEW_ONE_TO_ONE",
                "VIEW_FEEDBACKS",
                "VIEW_ASSESSMENT_GOALS",
                "VIEW_COMMENTS",
                "VIEW_ROLES",
                "VIEW_DEPARTMENTS",
                "EDIT_DEPARTMENTS",
                "EDIT_ROLES",
                "EDIT_INTERVALS"
        });

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);

        String tokenSecretKey = "test";
        return jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenSecretKey).compact();
    };

    @MockBean
    private MediaStorageCommandService mediaStorageCommandService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(authorities = "EMPLOYEE_CARD")
    void testControllerWithoutAuthorization() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "filename.png", "image/jpg", "some jpg".getBytes());

        BDDMockito.given(mediaStorageCommandService.saveFile("e45bfff0-1b04-4b8a-ac95-9629dff88a3e", file))
                .willReturn("somestr");

        mvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file("file", file.getBytes())
                .header("Authorization", "Bearer " + newToken.get())
                .characterEncoding("UTF-8"))
                .andExpect(status().is(202));
    }
}