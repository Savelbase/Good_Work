package com.rm.toolkit.oneToOne.command.message;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.event.user.UserCreatedEvent;
import com.rm.toolkit.oneToOne.command.event.user.UserEditedEvent;
import com.rm.toolkit.oneToOne.command.message.projector.UserProjector;
import com.rm.toolkit.oneToOne.command.model.User;
import com.rm.toolkit.oneToOne.command.repository.UserRepository;
import com.rm.toolkit.oneToOne.command.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {
    private final UserRepository userRepository;
    private final UserProjector userProjector;
    private final UserUtil userUtil;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserCreatedEvent event) {
        User user = userProjector.project(event);
        userRepository.save(user);

        log.info("Пользователь с id {} создан после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserEditedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} отредактирован после получения события", user.getId());
    }

    private User getUserFromEvent(Event<? extends EventPayload> event) {
        String userId = event.getEntityId();
        return userUtil.findUserIfExists(userId);
    }
}
