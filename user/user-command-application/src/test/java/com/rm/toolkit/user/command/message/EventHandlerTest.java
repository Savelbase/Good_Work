package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.auth.UserMaxLoginAttemptsReachedEvent;
import com.rm.toolkit.user.command.event.user.UserBlockedEvent;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.util.EventUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ContextConfiguration(classes = {EventHandler.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(UserProjector.class), @MockBean(UserRepository.class)
        , @MockBean(EventPublisher.class), @MockBean(EventUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class EventHandlerTest {
    private final EventHandler eventHandler;

    private final UserProjector userProjector;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;

    @Test
    void shouldHandleUserMaxLoginAttemptsReachedEvent() {
        String authorId = "user";
        String userId = "Test";
        int version = 0;
        UserMaxLoginAttemptsReachedEvent event = new UserMaxLoginAttemptsReachedEvent();
        event.setAuthor(authorId);
        event.setEntityId(userId);
        User user = new User();
        user.setId(userId);
        user.setVersion(version);
        UserBlockedEvent blockedEvent = new UserBlockedEvent();
        blockedEvent.setPayload(new UserBlockedEvent.Payload());


        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(eventUtil).populateEventFields(blockedEvent, user.getId()
                , user.getVersion() + 1, event.getAuthor(), blockedEvent.getPayload());
        Mockito.doNothing().when(userProjector).project(blockedEvent, user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(eventPublisher).publishWithReupload(blockedEvent);

        eventHandler.handle(event);

        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(eventUtil).populateEventFields(blockedEvent, user.getId()
                , user.getVersion() + 1, event.getAuthor(), blockedEvent.getPayload());
        Mockito.verify(userProjector).project(blockedEvent, user);
        Mockito.verify(userRepository).save(user);
        Mockito.verify(eventPublisher).publishWithReupload(blockedEvent);
    }
}