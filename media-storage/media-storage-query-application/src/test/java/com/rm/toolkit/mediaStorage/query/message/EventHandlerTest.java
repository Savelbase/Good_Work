package com.rm.toolkit.mediaStorage.query.message;

import com.rm.toolkit.mediaStorage.query.event.EventType;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.exception.IdNotFoundException;
import com.rm.toolkit.mediaStorage.query.util.MediaStorageQueryUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EventHandler.class})
@ExtendWith(SpringExtension.class)
class EventHandlerTest {
    @Autowired
    private EventHandler eventHandler;

    @MockBean
    private MediaStorageQueryUtil mediaStorageQueryUtil;

    private final Supplier<AvatarUploadedEvent> avatarUploadedEventSupplier = () -> {
        AvatarUploadedEvent avatarUploadedEvent = new AvatarUploadedEvent();
        avatarUploadedEvent.setEntityId("42");
        avatarUploadedEvent.setParentId("42");
        avatarUploadedEvent.setTime(null);
        avatarUploadedEvent.setVersion(1);
        avatarUploadedEvent.setPayload(new AvatarUploadedEvent.Payload());
        avatarUploadedEvent.setType(EventType.AVATAR_UPLOADED);
        avatarUploadedEvent.setId("42");
        avatarUploadedEvent.setContext("Context");
        avatarUploadedEvent.setAuthor("JaneDoe");
        return avatarUploadedEvent;
    };
    private final Supplier<AvatarConfirmedEvent> avatarConfirmedEventSupplier = () -> {
        AvatarConfirmedEvent avatarConfirmedEvent = new AvatarConfirmedEvent();
        avatarConfirmedEvent.setEntityId("42");
        avatarConfirmedEvent.setParentId("42");
        avatarConfirmedEvent.setTime(null);
        avatarConfirmedEvent.setVersion(1);
        avatarConfirmedEvent.setPayload(new AvatarConfirmedEvent.Payload());
        avatarConfirmedEvent.setType(EventType.AVATAR_UPLOADED);
        avatarConfirmedEvent.setId("42");
        avatarConfirmedEvent.setContext("Context");
        avatarConfirmedEvent.setAuthor("JaneDoe");
        return avatarConfirmedEvent;
    };
    private final Supplier<AvatarDeletedEvent> avatarDeletedEventSupplier = () -> {
        AvatarDeletedEvent avatarDeletedEvent = new AvatarDeletedEvent();
        avatarDeletedEvent.setEntityId("42");
        avatarDeletedEvent.setParentId("42");
        avatarDeletedEvent.setTime(null);
        avatarDeletedEvent.setVersion(1);
        avatarDeletedEvent.setPayload(new AvatarDeletedEvent.Payload());
        avatarDeletedEvent.setType(EventType.AVATAR_UPLOADED);
        avatarDeletedEvent.setId("42");
        avatarDeletedEvent.setContext("Context");
        avatarDeletedEvent.setAuthor("JaneDoe");
        return avatarDeletedEvent;
    };


    @Test
    void shouldHandleUploadAvatarCreatedEvent() {
        doNothing().when(this.mediaStorageQueryUtil).handleAvatarUploadedEvent((AvatarUploadedEvent) any());

        AvatarUploadedEvent avatarUploadedEvent = avatarUploadedEventSupplier.get();

        this.eventHandler.handle(avatarUploadedEvent);
        verify(this.mediaStorageQueryUtil).handleAvatarUploadedEvent((AvatarUploadedEvent) any());
    }

    @Test
    void shouldHandleAvatarConfirmedEvent() {
        doNothing().when(this.mediaStorageQueryUtil).handleAvatarConfirmedEvent((AvatarConfirmedEvent) any());

        AvatarConfirmedEvent avatarConfirmedEvent = avatarConfirmedEventSupplier.get();

        this.eventHandler.handle(avatarConfirmedEvent);
        verify(this.mediaStorageQueryUtil).handleAvatarConfirmedEvent((AvatarConfirmedEvent) any());
    }

    @Test
    void shouldThrowExceptionOnAvatarConfirmedEvent() {
        String id = "Test";
        doThrow(new IdNotFoundException(id)).when(this.mediaStorageQueryUtil)
                .handleAvatarConfirmedEvent((AvatarConfirmedEvent) any());

        AvatarConfirmedEvent avatarConfirmedEvent = avatarConfirmedEventSupplier.get();

        this.eventHandler.handle(avatarConfirmedEvent);
        verify(this.mediaStorageQueryUtil).handleAvatarConfirmedEvent((AvatarConfirmedEvent) any());
    }

    @Test
    void shouldHandleAvatarDeletedEvent() {
        doNothing().when(this.mediaStorageQueryUtil).handleAvatarDeletedEvent((AvatarDeletedEvent) any());

        AvatarDeletedEvent avatarDeletedEvent = avatarDeletedEventSupplier.get();

        this.eventHandler.handle(avatarDeletedEvent);
        verify(this.mediaStorageQueryUtil).handleAvatarDeletedEvent((AvatarDeletedEvent) any());
    }

    @Test
    void shouldThrowExceptionOnAvatarDeletedEvent() {
        String id = "Test";
        doThrow(new IdNotFoundException(id)).when(this.mediaStorageQueryUtil)
                .handleAvatarDeletedEvent((AvatarDeletedEvent) any());

        AvatarDeletedEvent avatarDeletedEvent = avatarDeletedEventSupplier.get();

        this.eventHandler.handle(avatarDeletedEvent);
        verify(this.mediaStorageQueryUtil).handleAvatarDeletedEvent((AvatarDeletedEvent) any());
    }
}

