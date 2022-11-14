package com.rm.toolkit.user.command.message;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.auth.UserMaxLoginAttemptsReachedEvent;
import com.rm.toolkit.user.command.event.user.UserBlockedEvent;
import com.rm.toolkit.user.command.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.util.EventUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final UserProjector userProjector;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserMaxLoginAttemptsReachedEvent event) {
        User user = getUserFromEvent(event);

        UserBlockedEvent userBlockedEvent = new UserBlockedEvent();
        eventUtil.populateEventFields(userBlockedEvent, user.getId(), user.getVersion() + 1, event.getAuthor(),
                new UserBlockedEvent.Payload());
        userProjector.project(userBlockedEvent, user);
        userRepository.save(user);
        eventPublisher.publishWithReupload(userBlockedEvent);

        log.info("Пользователь с id {} заблокирован после получения события", user.getId());
    }

    private User getUserFromEvent(Event<? extends EventPayload> event) {
        String userId = event.getEntityId();
        return userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователя с id {} не существует", userId);
            throw new UserNotFoundException(userId);
        });
    }
}
