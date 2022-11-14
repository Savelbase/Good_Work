package com.rm.toolkit.mediaStorage.command.message;

import com.rm.toolkit.mediaStorage.command.event.user.UserCreatedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserEditedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
    void testHandleUserCreatedEvent() {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent();
        gateway.handle(userCreatedEvent);
        Mockito.verify(handler).handle(userCreatedEvent);
    }

    @Test
    void testHandleUserEditedEvent() {
        UserEditedEvent userEditedEvent = new UserEditedEvent();
        gateway.handle(userEditedEvent);
        Mockito.verify(handler).handle(userEditedEvent);
    }
}