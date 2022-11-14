package com.rm.toolkit.comment.command.util;

import com.rm.toolkit.comment.command.event.Event;
import com.rm.toolkit.comment.command.event.EventPayload;
import com.rm.toolkit.comment.command.exception.ProjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class ProjectionUtil {
    public void validateEvent(Event<? extends EventPayload> event, String entityId, Integer entityCurrentVersion) {
        String commentIdFromEvent = event.getEntityId();
        if (!commentIdFromEvent.equals(entityId)) {
            log.error("id сущности {} и entityId события {} не совпадают", entityId, commentIdFromEvent);
            throw new ProjectionException(String.format("id сущности %s и entityId id события %s не совпадают", entityId,
                    commentIdFromEvent));
        }

        Integer entityVersionFromEvent = event.getVersion();
        if (entityVersionFromEvent != (entityCurrentVersion + 1)) {
            log.error("Версия события {} не подходит к сущности с версией {}", entityVersionFromEvent,
                    entityCurrentVersion);
            throw new ProjectionException(String.format("Версия события %s не подходит к сущности с версией %s",
                    entityVersionFromEvent, entityCurrentVersion));
        }
    }
}
