package com.rm.toolkit.oneToOne.query.message;

import com.rm.toolkit.oneToOne.query.event.Event;
import com.rm.toolkit.oneToOne.query.event.EventPayload;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserEditedEvent;
import com.rm.toolkit.oneToOne.query.exception.OneToOneNotFoundException;
import com.rm.toolkit.oneToOne.query.exception.UserNotFoundException;
import com.rm.toolkit.oneToOne.query.message.projector.OneToOneProjector;
import com.rm.toolkit.oneToOne.query.message.projector.UserProjector;
import com.rm.toolkit.oneToOne.query.model.OneToOne;
import com.rm.toolkit.oneToOne.query.model.User;
import com.rm.toolkit.oneToOne.query.repository.OneToOneRepository;
import com.rm.toolkit.oneToOne.query.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventHandler {

    private final OneToOneRepository oneToOneRepository;
    private final OneToOneProjector oneToOneProjector;
    private final UserProjector userProjector;
    private final UserRepository userRepository;


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneCreatedEvent event) {
        OneToOne oneToOne = oneToOneProjector.project(event);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} создан после получения события", oneToOne.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneUpdatedEvent event) {
        OneToOne oneToOne = getOneToOneFromEvent(event);
        oneToOneProjector.project(event, oneToOne);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} отредактирован после получения события", oneToOne.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneDeletedEvent event) {
        OneToOne oneToOne = getOneToOneFromEvent(event);
        oneToOneProjector.project(event, oneToOne);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} удалён после получения события", oneToOne.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneCompletedEvent event) {
        OneToOne oneToOne = getOneToOneFromEvent(event);
        oneToOneProjector.project(event, oneToOne);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} переведен в статус Завершенный после получения события", oneToOne.getId());

    }

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

    private OneToOne getOneToOneFromEvent(Event<? extends EventPayload> event) {
        String oneToOneId = event.getEntityId();
        return oneToOneRepository.findById(oneToOneId).orElseThrow(() -> {
            log.info("Пользователя с id {} не существует", oneToOneId);
            throw new OneToOneNotFoundException(oneToOneId);
        });
    }

    private User getUserFromEvent(Event<? extends EventPayload> event) {
        String userId = event.getEntityId();
        return userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователя с id {} не существует", userId);
            throw new UserNotFoundException(userId);
        });
    }
}
