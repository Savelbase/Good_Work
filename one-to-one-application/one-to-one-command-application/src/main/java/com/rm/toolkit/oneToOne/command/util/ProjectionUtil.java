package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.model.iface.Versionable;
import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.exception.ProjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectionUtil {

    public void validateEvent(Event<? extends EventPayload> event, String entityId, Integer entityCurrentVersion) {
        String oneToOneIdFromEvent = event.getEntityId();
        if (!oneToOneIdFromEvent.equals(entityId)) {
            log.error("id one-to-one {} и entityId события {} не сопадают", entityId, oneToOneIdFromEvent);
            throw new ProjectionException(String.format("id one-to-one %s и entityId id события %s не совпадают", entityId,
                    oneToOneIdFromEvent));
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
