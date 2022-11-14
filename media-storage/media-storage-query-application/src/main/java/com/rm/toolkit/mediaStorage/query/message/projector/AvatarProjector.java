package com.rm.toolkit.mediaStorage.query.message.projector;

import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.model.MediaFileData;
import com.rm.toolkit.mediaStorage.query.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarProjector {

    private final ProjectionUtil projectionUtil;

    public MediaFileData project(AvatarUploadedEvent event) {
        var payload = event.getPayload();

        return MediaFileData.builder()
                .id(event.getEntityId())
                .url(payload.getUrl())
                .isConfirmed(payload.isConfirmed())
                .uploadDate(payload.getUploadDate())
                .version(1)
                .build();
    }

    public void project(AvatarConfirmedEvent event, MediaFileData mediaFileData) {
        projectionUtil.validateEvent(event, mediaFileData.getId(), mediaFileData.getVersion());

        mediaFileData.setConfirmed(true);
        projectionUtil.incrementVersion(mediaFileData);
    }
}
