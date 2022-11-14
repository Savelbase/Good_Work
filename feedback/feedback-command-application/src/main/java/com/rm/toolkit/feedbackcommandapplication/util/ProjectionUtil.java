package com.rm.toolkit.feedbackcommandapplication.util;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.exception.ProjectionException;

import com.rm.toolkit.feedbackcommandapplication.model.type.Versionable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectionUtil {

    public void validateEvent(Event<? extends EventPayload> event, String entityId, Integer entityCurrentVersion) {
        String feedbackIdFromEvent = event.getEntityId();
        if (!feedbackIdFromEvent.equals(entityId)) {
            log.error("id сущности {} и entityId события {} не совпадают", entityId, feedbackIdFromEvent);
            throw new ProjectionException(String.format("id сущности %s и entityId id события %s не совпадают", entityId,
                    feedbackIdFromEvent));
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
