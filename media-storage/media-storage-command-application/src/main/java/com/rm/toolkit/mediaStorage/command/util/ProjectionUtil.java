package com.rm.toolkit.mediaStorage.command.util;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.exception.ProjectionException;
import com.rm.toolkit.mediaStorage.command.model.iface.Versionable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectionUtil {

    public void validateEvent(Event<? extends EventPayload> event, String entityId, Integer entityCurrentVersion) {
        String mediaFileIdFromEvent = event.getEntityId();
        if (!mediaFileIdFromEvent.equals(entityId)) {
            log.error("id сущности {} и entityId события {} не сопадают", entityId, mediaFileIdFromEvent);
            throw new ProjectionException(String.format("id сущности %s и entityId id события %s не сопадают", entityId,
                    mediaFileIdFromEvent));
        }

        Integer entityVersionFromEvent = event.getVersion();
        if (entityVersionFromEvent != (entityCurrentVersion + 1)) {
            log.error("Версия события {} не подходит к сущности с версией {}", entityVersionFromEvent,
                    entityCurrentVersion);
            throw new ProjectionException(String.format("Версия события %s не подходит к сущности с версией %s",
                    entityVersionFromEvent, entityCurrentVersion));
        }
    }

    public void incrementVersion(Versionable versionable) {
        versionable.setVersion(versionable.getVersion() + 1);
    }
}
