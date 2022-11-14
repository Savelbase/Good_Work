package com.rm.toolkit.mediaStorage.command.message;

import com.rm.toolkit.mediaStorage.command.event.EventType;
import com.rm.toolkit.mediaStorage.command.event.user.UserCreatedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserEditedEvent;
import com.rm.toolkit.mediaStorage.command.exception.MediaFileDataNotFoundException;
import com.rm.toolkit.mediaStorage.command.util.MediaStorageCommandUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.util.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EventHandler.class})
@ExtendWith(SpringExtension.class)
class EventHandlerTest {
    @Autowired
    private EventHandler eventHandler;

    @MockBean
    private MediaStorageCommandUtil mediaStorageCommandUtil;

    private final Supplier<UserCreatedEvent> userCreatedEventSupplier = () -> {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
        userCreatedEvent.setEntityId("42");
        userCreatedEvent.setParentId("42");
        userCreatedEvent.setTime(null);
        userCreatedEvent.setVersion(1);
        userCreatedEvent.setPayload(new UserCreatedEvent.Payload("Avatar Path"));
        userCreatedEvent.setType(EventType.AVATAR_UPLOADED);
        userCreatedEvent.setId("42");
        userCreatedEvent.setContext("Context");
        userCreatedEvent.setAuthor("JaneDoe");
        return userCreatedEvent;
    };
    private final Supplier<UserEditedEvent> userEditedEventSupplier = () -> {
        UserEditedEvent userEditedEvent = new UserEditedEvent();
        userEditedEvent.setEntityId("42");
        userEditedEvent.setParentId("42");
        userEditedEvent.setTime(null);
        userEditedEvent.setVersion(1);
        userEditedEvent.setPayload(new UserEditedEvent.Payload("Avatar Path"));
        userEditedEvent.setType(EventType.AVATAR_UPLOADED);
        userEditedEvent.setId("42");
        userEditedEvent.setContext("Context");
        userEditedEvent.setAuthor("JaneDoe");
        return userEditedEvent;
    };


    @Test
    void shouldHandleUserCreatedEvent() {
        doNothing().when(this.mediaStorageCommandUtil).handleUserCreatedEvent((UserCreatedEvent) any());

        UserCreatedEvent userCreatedEvent = userCreatedEventSupplier.get();

        this.eventHandler.handle(userCreatedEvent);
        verify(this.mediaStorageCommandUtil).handleUserCreatedEvent((UserCreatedEvent) any());
    }

    @Test
    void shouldThrowExceptionOnHandleUserCreatedEvent() {
        doThrow(new MediaFileDataNotFoundException("An error occurred")).when(this.mediaStorageCommandUtil)
                .handleUserCreatedEvent((UserCreatedEvent) any());

        UserCreatedEvent userCreatedEvent = userCreatedEventSupplier.get();

        this.eventHandler.handle(userCreatedEvent);
        verify(this.mediaStorageCommandUtil).handleUserCreatedEvent((UserCreatedEvent) any());
    }

    @Test
    void shouldHandleUserEditedEvent() {
        doNothing().when(this.mediaStorageCommandUtil).handleUserEditedEvent((UserEditedEvent) any());

        UserEditedEvent userEditedEvent = userEditedEventSupplier.get();

        this.eventHandler.handle(userEditedEvent);
        verify(this.mediaStorageCommandUtil).handleUserEditedEvent((UserEditedEvent) any());
    }

    @Test
    void shouldThrowExceptionOnHandleUserEditedEvent() {
        doThrow(new MediaFileDataNotFoundException("An error occurred")).when(this.mediaStorageCommandUtil)
                .handleUserEditedEvent((UserEditedEvent) any());

        UserEditedEvent userEditedEvent = userEditedEventSupplier.get();

        this.eventHandler.handle(userEditedEvent);
        verify(this.mediaStorageCommandUtil).handleUserEditedEvent((UserEditedEvent) any());
    }
}


