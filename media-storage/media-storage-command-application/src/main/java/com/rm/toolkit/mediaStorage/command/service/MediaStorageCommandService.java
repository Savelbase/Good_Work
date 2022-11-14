package com.rm.toolkit.mediaStorage.command.service;

import com.rm.toolkit.mediaStorage.command.event.avatar.UploadAvatarCreatedEvent;
import com.rm.toolkit.mediaStorage.command.exception.FileNotProvidedException;
import com.rm.toolkit.mediaStorage.command.message.EventPublisher;
import com.rm.toolkit.mediaStorage.command.message.projector.AvatarProjector;
import com.rm.toolkit.mediaStorage.command.model.MediaFileData;
import com.rm.toolkit.mediaStorage.command.repository.MediaStorageRepository;
import com.rm.toolkit.mediaStorage.command.util.EventUtil;
import com.rm.toolkit.mediaStorage.command.util.MediaStorageCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaStorageCommandService {

    @Value("${UPLOAD_DIR}")
    private String uploadPath;

    private final EventUtil eventUtil;
    private final EventPublisher eventPublisher;
    private final MediaStorageCommandUtil mediaStorageCommandUtil;
    private final AvatarProjector avatarProjector;
    private final MediaStorageRepository mediaStorageRepository;

    public String saveFile(String authorId, MultipartFile file) throws IOException, MimeTypeException {
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            mediaStorageCommandUtil.init();
        }
        String result;

        if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")) {
            mediaStorageCommandUtil.checkExtension(file);
            String filename = UUID.randomUUID().toString();
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
            Files.copy(file.getInputStream(), root.resolve(filename + "." + extension));

            UploadAvatarCreatedEvent.Payload payload = UploadAvatarCreatedEvent.Payload.builder()

                    .url(String.format("%s.%s", filename, extension))
                    .isConfirmed(false)
                    .uploadDate(new Date())
                    .build();
            UploadAvatarCreatedEvent event = new UploadAvatarCreatedEvent();
            event.setPayload(payload);
            eventUtil.populateEventFields(event, UUID.randomUUID().toString(), 1, authorId, payload, true);

            eventPublisher.publishNoReupload(event);

            MediaFileData mediaFileData = avatarProjector.project(event);
            result = mediaStorageRepository.save(mediaFileData).getId();
        } else {
            throw new FileNotProvidedException("Неверный формат файла");
        }
        return result;
    }
}