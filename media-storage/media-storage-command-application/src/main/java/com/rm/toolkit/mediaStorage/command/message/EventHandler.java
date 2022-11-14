package com.rm.toolkit.mediaStorage.command.message;

import com.rm.toolkit.mediaStorage.command.event.user.UserCreatedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserEditedEvent;
import com.rm.toolkit.mediaStorage.command.exception.MediaFileDataNotFoundException;
import com.rm.toolkit.mediaStorage.command.util.MediaStorageCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventHandler {

    private final MediaStorageCommandUtil util;

    /**
     * @param event - событие создания пользователя
     * @throws MediaFileDataNotFoundException - файл по id не найден
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserCreatedEvent event) {
        try {
            if (event.getPayload().getAvatarPath() != null) {
                util.handleUserCreatedEvent(event);
                log.info("Файл с id {} подтвержден после получения события", event.getPayload().getAvatarPath());
            } else {
                log.info("Аватар пользователя с id {} не предоставлен, при создании пользователя", event.getEntityId());
            }
        } catch (MediaFileDataNotFoundException e) {
            log.info("Файл с id {} не найден", e.getMessage());
        }
    }

    /**
     * @param event - событие редактирования пользователя
     * @throws MediaFileDataNotFoundException - файл по id не найден
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserEditedEvent event) {
        try {
            if (event.getPayload().getAvatarPath() != null) {
                util.handleUserEditedEvent(event);
            } else {
                log.info("Аватар пользователя с id {} не предоставлен, при редактировании пользователя", event.getEntityId());
            }
        } catch (MediaFileDataNotFoundException e) {
            log.info("Файл с id {} не найден", e.getMessage());
        }
    }
}
