package com.rm.toolkit.mediaStorage.query.message;

import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.exception.IdNotFoundException;
import com.rm.toolkit.mediaStorage.query.util.MediaStorageQueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventHandler {
    private final MediaStorageQueryUtil util;

    /**
     * @param event событие добавления аватара в базу
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(AvatarUploadedEvent event) {
        util.handleAvatarUploadedEvent(event);
        log.info("Файл с id {} добавлен после получения события", event.getEntityId());
    }

    /**
     * @param event событие подтверждения аватара
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(AvatarConfirmedEvent event) {
        try {
            util.handleAvatarConfirmedEvent(event);
            log.info("Файл с id {} подтвержден после получения события", event.getEntityId());
        } catch (IdNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @param event событие удаления аватара
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(AvatarDeletedEvent event) {
        try {
            util.handleAvatarDeletedEvent(event);
            log.info("Файл с id {} удален после получения события", event.getEntityId());
        } catch (IdNotFoundException e) {
            log.error(e.getMessage());
        }
    }


}
