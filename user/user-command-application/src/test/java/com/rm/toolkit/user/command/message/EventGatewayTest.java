package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.auth.UserMaxLoginAttemptsReachedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {EventGateway.class})
@ExtendWith(SpringExtension.class)
class EventGatewayTest {
    @Autowired
    private EventGateway gateway;

    @MockBean
    private EventHandler eventHandler;

    @Test
    void handleUserMaxLoginAttemptsReachedEvent() {
        UserMaxLoginAttemptsReachedEvent event = new UserMaxLoginAttemptsReachedEvent();
        Mockito.doNothing().when(eventHandler).handle(event);

        gateway.handle(event);
        Mockito.verify(eventHandler).handle(event);
    }
}