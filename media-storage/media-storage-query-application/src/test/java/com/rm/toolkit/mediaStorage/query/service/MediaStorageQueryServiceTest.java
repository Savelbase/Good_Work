package com.rm.toolkit.mediaStorage.query.service;

import com.rm.toolkit.mediaStorage.query.exception.IdNotFoundException;
import com.rm.toolkit.mediaStorage.query.util.MediaStorageQueryUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MediaStorageQueryService.class})
@ExtendWith(SpringExtension.class)
class MediaStorageQueryServiceTest {

    @MockBean
    private MediaStorageQueryUtil mediaStorageQueryUtil;

    @Autowired
    private MediaStorageQueryService service;

    @Test
    void shouldThrowExceptionOnLoadFileById() {
        String id = "Id";
        doThrow(IdNotFoundException.class).when(this.mediaStorageQueryUtil).getFile(id);

        assertThrows(IdNotFoundException.class, () -> this.service.loadFileById(id));

        verify(this.mediaStorageQueryUtil).getFile(id);
    }

    @Test
    void shouldLoadFileById() {
        String id = "Id";
        byte[] file = "Test".getBytes();
        doReturn(file).when(this.mediaStorageQueryUtil).getFile(id);

        assertEquals(byte[].class, service.loadFileById(id).getClass());

        verify(this.mediaStorageQueryUtil).getFile(id);
    }
}