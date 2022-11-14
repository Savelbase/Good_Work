package com.rm.toolkit.mediaStorage.command.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.rm.toolkit.mediaStorage.command.exception.FileNotProvidedException;
import com.rm.toolkit.mediaStorage.command.message.EventPublisher;
import com.rm.toolkit.mediaStorage.command.message.projector.AvatarProjector;
import com.rm.toolkit.mediaStorage.command.repository.MediaStorageRepository;
import com.rm.toolkit.mediaStorage.command.util.EventUtil;
import com.rm.toolkit.mediaStorage.command.util.MediaStorageCommandUtil;

import java.io.IOException;

import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MediaStorageCommandService.class, String.class})
@ExtendWith(SpringExtension.class)
class MediaStorageCommandServiceTest {
    @MockBean
    private AvatarProjector avatarProjector;

    @MockBean
    private EventPublisher eventPublisher;

    @MockBean
    private EventUtil eventUtil;

    @Autowired
    private MediaStorageCommandService mediaStorageCommandService;

    @MockBean
    private MediaStorageCommandUtil mediaStorageCommandUtil;

    @MockBean
    private MediaStorageRepository mediaStorageRepository;

    @Test
    void testSaveFile() throws IOException, MimeTypeException {
        doNothing().when(this.mediaStorageCommandUtil).init();
        assertThrows(FileNotProvidedException.class, () -> this.mediaStorageCommandService.saveFile("42",
                new MockMultipartFile("Name", "AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8"))));
    }
}

