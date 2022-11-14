package com.rm.toolkit.mediaStorage.query.util;

import com.rm.toolkit.mediaStorage.query.event.Event;
import com.rm.toolkit.mediaStorage.query.event.EventPayload;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.exception.FileReadException;
import com.rm.toolkit.mediaStorage.query.exception.IdNotFoundException;
import com.rm.toolkit.mediaStorage.query.message.projector.AvatarProjector;
import com.rm.toolkit.mediaStorage.query.model.MediaFileData;
import com.rm.toolkit.mediaStorage.query.repository.MediaStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@RequiredArgsConstructor
@Slf4j
public class MediaStorageQueryUtil {
    private final MediaStorageRepository mediaStorageRepository;
    private final AvatarProjector avatarProjector;

    @Value("${UPLOAD_DIR}")
    private String uploadPath;

    /**
     * @param id аватара
     * @return аватар в формате byte[]
     * @throws FileReadException не получилось прочитать файл
     */
    public byte[] getFile(String id) {
        MediaFileData data = mediaStorageRepository.findFirstById(id).orElseThrow(() -> new IdNotFoundException(id));
        File file = new File(uploadPath + File.separator + data.getUrl());
        byte[] result;
        try {
            result = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new FileReadException(e.getMessage());
        }
        return result;
    }

    public void handleAvatarUploadedEvent(AvatarUploadedEvent event) {
        MediaFileData mediaFileData = avatarProjector.project(event);
        mediaStorageRepository.save(mediaFileData);
    }

    public void handleAvatarConfirmedEvent(AvatarConfirmedEvent event) {
        MediaFileData mediaFileData = getUserAvatarFromEvent(event);
        avatarProjector.project(event, mediaFileData);
        mediaStorageRepository.save(mediaFileData);
    }

    public void handleAvatarDeletedEvent(AvatarDeletedEvent event) {
        MediaFileData mediaFileData = getUserAvatarFromEvent(event);
        mediaStorageRepository.delete(mediaFileData);
    }

    /**
     * @param event событие аватара
     * @return MediaFileData сущность из таблицы media-storage-query
     * @throws IdNotFoundException аватар не найден по id
     */
    private MediaFileData getUserAvatarFromEvent(Event<? extends EventPayload> event) {
        String avatarId = event.getEntityId();
        return mediaStorageRepository.findById(avatarId).orElseThrow(() -> {
            log.info("Аватар с id {} не найден", avatarId);
            throw new IdNotFoundException(avatarId);
        });
    }
}
