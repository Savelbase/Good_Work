package com.rm.toolkit.mediaStorage.query.util;

import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.exception.FileReadException;
import com.rm.toolkit.mediaStorage.query.exception.IdNotFoundException;
import com.rm.toolkit.mediaStorage.query.message.projector.AvatarProjector;
import com.rm.toolkit.mediaStorage.query.model.MediaFileData;
import com.rm.toolkit.mediaStorage.query.repository.MediaStorageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MediaStorageQueryUtil.class})
@ExtendWith(SpringExtension.class)
class MediaStorageQueryUtilTest {
    @Autowired
    private MediaStorageQueryUtil util;

    @MockBean
    private MediaStorageRepository mediaStorageRepository;

    @MockBean
    private AvatarProjector avatarProjector;

    @Test
    void shouldThrowExceptionOnGetFile() throws IOException {
        String id = "Id";
        MediaFileData data = new MediaFileData();
        doReturn(Optional.of(data)).when(this.mediaStorageRepository).findFirstById(id);

        assertThrows(FileReadException.class, () -> util.getFile(id));

        verify(mediaStorageRepository).findFirstById(id);
    }

    @Test
    void shouldHandleAvatarUploadedEvent() {
        AvatarUploadedEvent event = new AvatarUploadedEvent();
        MediaFileData testData = new MediaFileData();
        when(avatarProjector.project(event)).thenReturn(testData);
        when(mediaStorageRepository.save(testData)).thenReturn(testData);

        util.handleAvatarUploadedEvent(event);

        verify(avatarProjector).project(event);
        verify(mediaStorageRepository).save(testData);
    }

    @Test
    void shouldHandleAvatarConfirmedEvent() {
        String testId = "Id";
        AvatarConfirmedEvent event = new AvatarConfirmedEvent();
        event.setEntityId(testId);
        MediaFileData testData = new MediaFileData();

        when(mediaStorageRepository.findById(testId)).thenReturn(Optional.of(testData));
        doNothing().when(avatarProjector).project(event, testData);
        when(mediaStorageRepository.save(testData)).thenReturn(testData);

        util.handleAvatarConfirmedEvent(event);

        verify(avatarProjector).project(event, testData);
        verify(mediaStorageRepository).save(testData);
        verify(mediaStorageRepository).findById(testId);
    }

    @Test
    void shouldThrowExceptionOnHandleAvatarConfirmedEvent() {
        String testId = "Id";
        AvatarConfirmedEvent event = new AvatarConfirmedEvent();
        event.setEntityId(testId);

        assertThrows(IdNotFoundException.class, () -> util.handleAvatarConfirmedEvent(event));
        verify(mediaStorageRepository).findById(testId);
        verify(mediaStorageRepository, times(0)).save(any());
    }

    @Test
    void shouldHandleAvatarDeletedEvent() {
        String testId = "Id";
        AvatarDeletedEvent event = new AvatarDeletedEvent();
        event.setEntityId(testId);
        MediaFileData testData = new MediaFileData();

        when(mediaStorageRepository.findById(testId)).thenReturn(Optional.of(testData));
        doNothing().when(mediaStorageRepository).delete(testData);

        util.handleAvatarDeletedEvent(event);

        verify(mediaStorageRepository).delete(testData);
        verify(mediaStorageRepository).findById(testId);
    }

    @Test
    void shouldThrowExceptionOnAvatarDeletedEvent() {
        String testId = "Id";
        AvatarDeletedEvent event = new AvatarDeletedEvent();
        event.setEntityId(testId);

        assertThrows(IdNotFoundException.class, () -> util.handleAvatarDeletedEvent(event));
        verify(mediaStorageRepository).findById(testId);
        verify(mediaStorageRepository, times(0)).delete(any());
    }
}