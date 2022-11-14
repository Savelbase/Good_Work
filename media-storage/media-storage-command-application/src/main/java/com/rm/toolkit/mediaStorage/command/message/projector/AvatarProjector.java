package com.rm.toolkit.mediaStorage.command.message.projector;

import com.rm.toolkit.mediaStorage.command.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.command.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.command.event.avatar.UploadAvatarCreatedEvent;
import com.rm.toolkit.mediaStorage.command.model.MediaFileData;
import com.rm.toolkit.mediaStorage.command.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarProjector {

    private final ProjectionUtil projectionUtil;

    public MediaFileData project(UploadAvatarCreatedEvent event) {
        var payload = event.getPayload();

        return MediaFileData.builder()
                .id(event.getEntityId())
                .url(event.getPayload().getUrl())
                .isConfirmed(event.getPayload().isConfirmed())
                .uploadDate(event.getPayload().getUploadDate())
                .version(1)
                .build();

    }

    public void project(AvatarConfirmedEvent event, MediaFileData mediaFileData) {
        projectionUtil.validateEvent(event, mediaFileData.getId(), mediaFileData.getVersion());
        mediaFileData.setConfirmed(true);
        projectionUtil.incrementVersion(mediaFileData);
    }

    public void project(AvatarDeletedEvent event, MediaFileData mediaFileData) {
        projectionUtil.validateEvent(event, mediaFileData.getId(), mediaFileData.getVersion());
        projectionUtil.incrementVersion(mediaFileData);
    }
}