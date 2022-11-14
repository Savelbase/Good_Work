package com.rm.toolkit.mediaStorage.command.util;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.command.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserCreatedEvent;
import com.rm.toolkit.mediaStorage.command.event.user.UserEditedEvent;
import com.rm.toolkit.mediaStorage.command.exception.CreationFolderException;
import com.rm.toolkit.mediaStorage.command.exception.MediaFileDataNotFoundException;
import com.rm.toolkit.mediaStorage.command.exception.UnsupportedEventException;
import com.rm.toolkit.mediaStorage.command.message.EventPublisher;
import com.rm.toolkit.mediaStorage.command.message.projector.AvatarProjector;
import com.rm.toolkit.mediaStorage.command.model.MediaFileData;
import com.rm.toolkit.mediaStorage.command.repository.MediaStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class MediaStorageCommandUtil {

    private final AvatarProjector avatarProjector;
    private final MediaStorageRepository mediaStorageRepository;
    private final EventUtil eventUtil;
    private final EventPublisher eventPublisher;

    public static final String PNG = "image/png";
    public static final String JPEG = "image/jpeg";

    @Value("${UPLOAD_DIR}")
    private String uploadPath;

    /**
     * @throws CreationFolderException не получилось создать папку для загрузки файлов
     */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new CreationFolderException("Не удалось создать папку для загрузок");
        }
    }

    /**
     * @param file файл, у которого проверяется расширение
     * @throws IOException       неполучилось прочитать файл
     * @throws MimeTypeException неверный формат
     */
    public void checkExtension(MultipartFile file) throws IOException, MimeTypeException {
        Tika tika = new Tika();
        String fileType = tika.detect(file.getBytes());
        if (!(Objects.equals(fileType, PNG)
                || Objects.equals(fileType, JPEG))
        ) {
            log.error("Ошибка формата файла");
            throw new MimeTypeException("Неверный формат файла " + fileType);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteFiles() {
        List<MediaFileData> mediaFiles = mediaStorageRepository.findAllByExpiryDateLessThan(Date.valueOf(LocalDate.now()));
        String cleanupName = "Scheduled Cleanup";

        for (MediaFileData mediaFile : mediaFiles) {
            deleteAvatarAndPublishEvent(mediaFile, cleanupName);
        }
    }

    /**
     * @param event - событие создания пользователя
     * @throws MediaFileDataNotFoundException - файл по Payload.avatarPath не найден
     */
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        publishAvatarConfirmedEvent(event);
    }

    /**
     * Если пользователь не найден, то ему назначается новый аватар. Если пользователь найден, то старый аватар удаляется,
     * и ему назначается новый аватар.
     * Обработка события редактирования пользователя.
     *
     * @param event событие редактирования пользователя
     * @throws NullPointerException           - пользователь ранее не был добавлен в базу
     * @throws MediaFileDataNotFoundException - файл по Payload.avatarPath не найден
     */
    public void handleUserEditedEvent(UserEditedEvent event) {
        MediaFileData mediaFileData = getUserAvatarFromEvent(event);
        try {
            if (isAvatarChanged(mediaFileData.getId(), event.getEntityId())) {
                MediaFileData oldAvatar = mediaStorageRepository.findFirstByUserId(event.getEntityId());
                deleteAvatarAndPublishEvent(oldAvatar, event.getAuthor());
            }
        } catch (NullPointerException e) {
            log.info("Пользователь с id = {} не найден в базе", event.getEntityId());
        } finally {
            publishAvatarConfirmedEvent(event);
        }
    }

    /**
     * Удаление аватара пользователя и публикация события для связанного сервиса
     *
     * @param mediaFile      файл для удаления
     * @param deletionSource источник, инициировавший удаление
     */
    private void deleteAvatarAndPublishEvent(MediaFileData mediaFile, String deletionSource) {
        File file = new File((uploadPath + File.separator + mediaFile.getUrl()));
        file.delete();
        mediaStorageRepository.delete(mediaFile);

        AvatarDeletedEvent avatarDeletedEvent = new AvatarDeletedEvent();
        eventUtil.populateEventFields(avatarDeletedEvent, mediaFile.getId(), mediaFile.getVersion() + 1, deletionSource,
                new AvatarDeletedEvent.Payload());
        avatarProjector.project(avatarDeletedEvent, mediaFile);
        eventPublisher.publishNoReupload(avatarDeletedEvent);
    }

    private boolean isAvatarChanged(String oldAvatarId, String userId) {
        MediaFileData mediaFileData = mediaStorageRepository.findFirstByUserId(userId);
        return !oldAvatarId.equals(mediaFileData.getId());
    }

    /**
     * @param event событие создания пользователя
     * @return MediaFileData с id = avatarPath события
     * @throws MediaFileDataNotFoundException сущность не найдена
     */
    private MediaFileData getUserAvatarFromEvent(UserCreatedEvent event) {
        String avatarId = event.getPayload().getAvatarPath();
        return mediaStorageRepository.findById(avatarId).orElseThrow(() -> {
            throw new MediaFileDataNotFoundException(avatarId);
        });
    }

    /**
     * @param event событие редактирования пользователя
     * @return MediaFileData с id = avatarPath события
     * @throws MediaFileDataNotFoundException сущность не найдена
     */
    private MediaFileData getUserAvatarFromEvent(UserEditedEvent event) {
        String avatarId = event.getPayload().getAvatarPath();
        return mediaStorageRepository.findById(avatarId).orElseThrow(() -> {
            throw new MediaFileDataNotFoundException(avatarId);
        });
    }

    /**
     * @param event событие, для подтверждения аватара пользователя
     * @throws UnsupportedEventException      необрабатываемое событие
     * @throws MediaFileDataNotFoundException - файл по Payload.avatarPath не найден
     */
    private void publishAvatarConfirmedEvent(Event<? extends EventPayload> event) {
        MediaFileData mediaFileData;
        if (event instanceof UserCreatedEvent) {
            mediaFileData = getUserAvatarFromEvent((UserCreatedEvent) event);
        } else if (event instanceof UserEditedEvent) {
            mediaFileData = getUserAvatarFromEvent((UserEditedEvent) event);
        } else {
            throw new UnsupportedEventException("Ошибка обработки события");
        }
        mediaFileData.setUserId(event.getEntityId());

        AvatarConfirmedEvent avatarConfirmedEvent = new AvatarConfirmedEvent();
        eventUtil.populateEventFields(avatarConfirmedEvent, mediaFileData.getId(), mediaFileData.getVersion() + 1, event.getAuthor(),
                new AvatarConfirmedEvent.Payload());
        avatarProjector.project(avatarConfirmedEvent, mediaFileData);
        mediaStorageRepository.save(mediaFileData);
        eventPublisher.publishNoReupload(avatarConfirmedEvent);
    }
}
