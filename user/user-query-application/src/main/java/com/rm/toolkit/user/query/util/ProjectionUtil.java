package com.rm.toolkit.user.query.util;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.exception.ProjectionException;
import com.rm.toolkit.user.query.model.embedded.ActivityEmbedded;
import com.rm.toolkit.user.query.model.iface.Versionable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class ProjectionUtil {

    public void validateEvent(Event<? extends EventPayload> event, String entityId, Integer entityCurrentVersion) {
        String userIdFromEvent = event.getEntityId();
        if (!userIdFromEvent.equals(entityId)) {
            log.error("id сущности {} и entityId события {} не совпадают", entityId, userIdFromEvent);
            throw new ProjectionException(String.format("id сущности %s и entityId id события %s не совпадают",
                    entityId,userIdFromEvent));
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

    public Set<ActivityEmbedded> convertMapToActivitiesEmbedded(Map<String, String> activityMap) {
        Set<ActivityEmbedded> activities = new HashSet<>();
        for (Map.Entry<String, String> entry : activityMap.entrySet()) {
            activities.add(new ActivityEmbedded(entry.getKey(), entry.getValue()));
        }
        return activities;
    }
}
