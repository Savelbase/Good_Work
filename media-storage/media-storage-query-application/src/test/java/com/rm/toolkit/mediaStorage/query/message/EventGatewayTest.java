package com.rm.toolkit.mediaStorage.query.message;

import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EventGateway.class})
@ExtendWith(SpringExtension.class)
class EventGatewayTest {
    @MockBean
    private EventHandler handler;
    @Autowired
    private EventGateway gateway;

    @Test
    void testHandleUploadAvatarCreatedEvent() {
        AvatarUploadedEvent avatarUploadedEvent = new AvatarUploadedEvent();
        gateway.handle(avatarUploadedEvent);
        Mockito.verify(handler).handle(avatarUploadedEvent);
    }

    @Test
    void testHandleAvatarConfirmedEvent() {
        AvatarConfirmedEvent avatarConfirmedEvent = new AvatarConfirmedEvent();
        gateway.handle(avatarConfirmedEvent);
        Mockito.verify(handler).handle(avatarConfirmedEvent);
    }

    @Test
    void testHandleAvatarDeletedEvent() {
        AvatarDeletedEvent avatarDeletedEvent = new AvatarDeletedEvent();
        gateway.handle(avatarDeletedEvent);
        Mockito.verify(handler).handle(avatarDeletedEvent);
    }
}