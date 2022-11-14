package com.rm.toolkit.mediaStorage.command.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.event.EventType;
import com.rm.toolkit.mediaStorage.command.event.UnsentEvent;
import com.rm.toolkit.mediaStorage.command.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.command.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserCreatedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserEditedEvent;
import com.rm.toolkit.mediaStorage.command.exception.MediaFileDataNotFoundException;
import com.rm.toolkit.mediaStorage.command.exception.UnsupportedEventException;
import com.rm.toolkit.mediaStorage.command.message.EventPublisher;
import com.rm.toolkit.mediaStorage.command.message.EventSender;
import com.rm.toolkit.mediaStorage.command.message.projector.AvatarProjector;
import com.rm.toolkit.mediaStorage.command.model.MediaFileData;
import com.rm.toolkit.mediaStorage.command.repository.EventRepository;
import com.rm.toolkit.mediaStorage.command.repository.MediaStorageRepository;
import com.rm.toolkit.mediaStorage.command.repository.UnsentEventRepository;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.util.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {MediaStorageCommandUtil.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(MediaStorageRepository.class), @MockBean(EventPublisher.class),
        @MockBean(EventUtil.class), @MockBean(AvatarProjector.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MediaStorageCommandUtilTest {
    private final MediaStorageCommandUtil mediaStorageCommandUtil;

    private final AvatarProjector avatarProjector;
    private final MediaStorageRepository mediaStorageRepository;
    private final EventUtil eventUtil;
    private final EventPublisher eventPublisher;

    private final Supplier<MediaFileData> mediaFileDataSupplier = () -> {
        MediaFileData mediaFileData = new MediaFileData();
        mediaFileData.setVersion(1);
        mediaFileData.setUserId("42");
        mediaFileData.setId("42");
        mediaFileData.setUrl("https://example.org/example");
        mediaFileData.setConfirmed(true);
        return mediaFileData;
    };

    private final Supplier<UserCreatedEvent> userCreatedEventSupplier = () -> {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
        userCreatedEvent.setId("Test");
        userCreatedEvent.setAuthor("Test");
        userCreatedEvent.setContext("Test");
        userCreatedEvent.setTime(null);
        userCreatedEvent.setEntityId("Test");
        userCreatedEvent.setParentId("Test");
        userCreatedEvent.setVersion(0);
        UserCreatedEvent.Payload payload = new UserCreatedEvent.Payload();
        payload.setAvatarPath("Test");
        userCreatedEvent.setPayload(payload);
        return userCreatedEvent;
    };

    private final Supplier<UserEditedEvent> userEditedEventSupplier = () -> {
        UserEditedEvent userEditedEvent = new UserEditedEvent();
        userEditedEvent.setId("Test");
        userEditedEvent.setAuthor("Test");
        userEditedEvent.setContext("Test");
        userEditedEvent.setTime(null);
        userEditedEvent.setEntityId("Test");
        userEditedEvent.setParentId("Test");
        userEditedEvent.setVersion(0);
        UserEditedEvent.Payload payload = new UserEditedEvent.Payload();
        payload.setAvatarPath("Test");
        userEditedEvent.setPayload(payload);
        return userEditedEvent;
    };

    @Test
    void testCheckExtension() throws IOException, MimeTypeException {
        AvatarProjector avatarProjector = new AvatarProjector(new ProjectionUtil());
        EventUtil eventUtil = new EventUtil();
        MediaStorageCommandUtil mediaStorageCommandUtil = new MediaStorageCommandUtil(avatarProjector,
                mediaStorageRepository, eventUtil, eventPublisher);
        assertThrows(MimeTypeException.class, () -> mediaStorageCommandUtil
                .checkExtension(new MockMultipartFile("Name", "AAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8"))));
    }

    @Test
    void shouldThrowMimeTypeExceptionOnCheckExtension() throws IOException, MimeTypeException {
        AvatarProjector avatarProjector = new AvatarProjector(new ProjectionUtil());
        MediaStorageRepository mediaStorageRepository = mock(MediaStorageRepository.class);
        EventUtil eventUtil = new EventUtil();
        MediaStorageCommandUtil mediaStorageCommandUtil = new MediaStorageCommandUtil(avatarProjector,
                mediaStorageRepository, eventUtil,
                new EventPublisher(mock(EventSender.class), mock(EventRepository.class), mock(UnsentEventRepository.class)));
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("AAAAAAAA".getBytes("UTF-8"));
        assertThrows(MimeTypeException.class, () -> mediaStorageCommandUtil.checkExtension(multipartFile));
        verify(multipartFile).getBytes();
    }

    @Test
    void handleUserCreatedEvent() {
        UserCreatedEvent event = userCreatedEventSupplier.get();
        MediaFileData mediaFileData = mediaFileDataSupplier.get();

        when(mediaStorageRepository.findById(anyString())).thenReturn(Optional.of(mediaFileData));

        mediaStorageCommandUtil.handleUserCreatedEvent(event);

        verify(eventUtil).populateEventFields(isA(AvatarConfirmedEvent.class), isA(String.class), isA(Integer.class), isA(String.class),
                isA(AvatarConfirmedEvent.Payload.class));
        verify(avatarProjector).project(isA(AvatarConfirmedEvent.class), isA(MediaFileData.class));
        verify(mediaStorageRepository).save(isA(MediaFileData.class));
        verify(eventPublisher).publishNoReupload(isA(AvatarConfirmedEvent.class));
    }

    @Test
    void shouldThrowUnsupportedEventExceptionOnHandleUserCreatedEvent() {
        UserCreatedEvent event = null;
        assertThrows(UnsupportedEventException.class, () -> mediaStorageCommandUtil.handleUserCreatedEvent(event));
    }

    @Test
    void shouldThrowMediaFileDataNotFoundExceptionOnHandleUserCreatedEvent() {
        String avatarPath = "Test";
        UserCreatedEvent event = new UserCreatedEvent();
        event.setPayload(UserCreatedEvent.Payload.builder().avatarPath(avatarPath).build());
        when(mediaStorageRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(MediaFileDataNotFoundException.class, () -> mediaStorageCommandUtil.handleUserCreatedEvent(event));
    }

    @Test
    void testHandleUserEditedEventAndDeleteOldAvatar() {
        MediaFileData mediaFileData = mediaFileDataSupplier.get();
        mediaFileData.setId("Random Id");
        UserEditedEvent event = userEditedEventSupplier.get();

        when(mediaStorageRepository.findById(anyString())).thenReturn(Optional.of(mediaFileData));
        when(mediaStorageRepository.findFirstByUserId(anyString())).thenReturn(mediaFileDataSupplier.get());

        mediaStorageCommandUtil.handleUserEditedEvent(event);

        verify(mediaStorageRepository).delete(isA(MediaFileData.class));
        verify(eventUtil).populateEventFields(isA(AvatarDeletedEvent.class), isA(String.class), isA(Integer.class), isA(String.class),
                isA(AvatarDeletedEvent.Payload.class));
        verify(avatarProjector).project(isA(AvatarDeletedEvent.class), isA(MediaFileData.class));
        verify(eventPublisher).publishNoReupload(isA(AvatarDeletedEvent.class));

        verify(eventUtil).populateEventFields(isA(AvatarConfirmedEvent.class), isA(String.class), isA(Integer.class), isA(String.class),
                isA(AvatarConfirmedEvent.Payload.class));
        verify(avatarProjector).project(isA(AvatarConfirmedEvent.class), isA(MediaFileData.class));
        verify(mediaStorageRepository).save(isA(MediaFileData.class));
        verify(eventPublisher).publishNoReupload(isA(AvatarConfirmedEvent.class));
    }

    @Test
    void testHandleUserEditedEventWithSameAvatar() {
        MediaFileData mediaFileData = mediaFileDataSupplier.get();
        UserEditedEvent event = userEditedEventSupplier.get();

        when(mediaStorageRepository.findById(anyString())).thenReturn(Optional.of(mediaFileData));
        when(mediaStorageRepository.findFirstByUserId(anyString())).thenReturn(mediaFileData);

        mediaStorageCommandUtil.handleUserEditedEvent(event);

        verify(mediaStorageRepository, times(0)).delete(isA(MediaFileData.class));
        verify(eventUtil, times(0)).populateEventFields(isA(AvatarDeletedEvent.class), isA(String.class), isA(Integer.class), isA(String.class),
                isA(AvatarDeletedEvent.Payload.class));
        verify(avatarProjector, times(0)).project(isA(AvatarDeletedEvent.class), isA(MediaFileData.class));
        verify(eventPublisher, times(0)).publishNoReupload(isA(AvatarDeletedEvent.class));

        verify(eventUtil).populateEventFields(isA(AvatarConfirmedEvent.class), isA(String.class), isA(Integer.class), isA(String.class),
                isA(AvatarConfirmedEvent.Payload.class));
        verify(avatarProjector).project(isA(AvatarConfirmedEvent.class), isA(MediaFileData.class));
        verify(mediaStorageRepository).save(isA(MediaFileData.class));
        verify(eventPublisher).publishNoReupload(isA(AvatarConfirmedEvent.class));
    }

    @Test
    void shouldThrowMediaFileDataNotFoundExceptionOnHandleUserEditedEvent() {
        UserEditedEvent event = userEditedEventSupplier.get();

        when(mediaStorageRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(MediaFileDataNotFoundException.class, () -> mediaStorageCommandUtil.handleUserEditedEvent(event));
    }
}

