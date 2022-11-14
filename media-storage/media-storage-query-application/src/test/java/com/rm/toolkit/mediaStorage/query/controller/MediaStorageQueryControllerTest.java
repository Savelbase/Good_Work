package com.rm.toolkit.mediaStorage.query.controller;

import com.rm.toolkit.mediaStorage.query.service.MediaStorageQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MediaStorageQueryController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(MediaStorageQueryController.class)
class MediaStorageQueryControllerTest {

    @MockBean
    private MediaStorageQueryService service;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testControllerWithoutAuthorization() throws Exception {
        mvc.perform(get("/api/files/1").contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().is(401));
    }
}