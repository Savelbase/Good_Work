package com.rm.toolkit.auth.util;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventUtil {

    @Value("${spring.application.name}")
    private String context;

    public static Long uuidStringToLong(String uuid) {
        return UUID.fromString(uuid).getMostSignificantBits() & Long.MAX_VALUE;
    }

    /**
     * Заполнить все поля некаскадного Event. Event ID случайный.
     *
     * @param event    событие
     * @param entityId ID объекта, с которым связано событие
     * @param version  новая версия события, обычно должно быть на 1 больше текущего
     * @param authorId ID юзера, создавшего событие
     * @param payload  payload события
     */
    public <T extends EventPayload, S extends Event<T>> void populateEventFields(S event, String entityId,
                                                                                 Integer version, String authorId,
                                                                                 T payload) {
        populateEventFields(event, entityId, version, authorId, payload, true, null);
    }

    /**
     * Заполнить все поля некаскадного Event. Заполнится ли Event ID зависит т randomEventId.
     *
     * @param event         событие
     * @param entityId      ID объекта, с которым связано событие
     * @param version       новая версия события, обычно должно быть на 1 больше текущего
     * @param authorId      ID юзера, создавшего событие
     * @param payload       payload события
     * @param randomEventId если true, то Event ID будет случайный; если false, то поле не заполняется
     */
    public <T extends EventPayload, S extends Event<T>> void populateEventFields(S event, String entityId,
                                                                                 Integer version, String authorId,
                                                                                 T payload, boolean randomEventId) {
        populateEventFields(event, entityId, version, authorId, payload, randomEventId, null);
    }

    /**
     * Заполнить все поля каскадного Event. Event ID случайный.
     *
     * @param event    событие
     * @param entityId ID объекта, с которым связано событие
     * @param version  новая версия события, обычно должно быть на 1 больше текущего
     * @param authorId ID юзера, создавшего событие
     * @param payload  payload события
     */
    public <T extends EventPayload, S extends Event<T>> void populateEventFields(S event, String entityId,
                                                                                 Integer version, String authorId,
                                                                                 T payload, String parentId) {
        populateEventFields(event, entityId, version, authorId, payload, true, parentId);
    }


    /**
     * Заполнить все поля каскадного Event. Заполнится ли Event ID зависит от randomEventId.
     *
     * @param event         событие
     * @param entityId      ID объекта, с которым связано событие
     * @param version       новая версия события, обычно должно быть на 1 больше текущего
     * @param authorId      ID юзера, создавшего событие
     * @param payload       payload события
     * @param randomEventId если true, то Event ID будет случайный; если false, то поле не заполняется
     * @param parentId      Event ID, которое стало первопричиной этого события
     */
    public <T extends EventPayload, S extends Event<T>> void populateEventFields(S event, String entityId,
                                                                                 Integer version, String authorId,
                                                                                 T payload, boolean randomEventId,
                                                                                 String parentId) {
        if (randomEventId) {
            event.setId(UUID.randomUUID().toString());
        }
        event.setEntityId(entityId);
        event.setAuthor(authorId);
        event.setContext(context);
        event.setTime(Instant.now());
        event.setVersion(version);
        event.setParentId(parentId);
        event.setPayload(payload);
    }
}
