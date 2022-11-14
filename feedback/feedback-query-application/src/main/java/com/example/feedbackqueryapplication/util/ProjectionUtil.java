package com.example.feedbackqueryapplication.util;

import com.example.feedbackqueryapplication.event.Event;
import com.example.feedbackqueryapplication.event.EventPayload;
import com.example.feedbackqueryapplication.exception.ProjectionException;
import com.example.feedbackqueryapplication.model.Versionable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectionUtil {

    public void validateEvent(Event<? extends EventPayload> event, String entityId, Integer entityCurrentVersion) {
        String feedbackIdFromEvent = event.getEntityId();
        if (!feedbackIdFromEvent.equals(entityId)) {
            log.error("id сущности {} и entityId события {} не сопадают", entityId, feedbackIdFromEvent);
            throw new ProjectionException(String.format("id сущности %s и entityId id события %s не сопадают", entityId,
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
