package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.util.EventUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = EventUtil.class)
@ExtendWith(SpringExtension.class)
public class EventUtilTest {
    private final String entityId = "cfd7ed64-4d37-48c2-af7c-ea965fa2f23b";
    private final String authorId = "7368017b-f0d5-4e54-bbe4-72144e4d20e4";
    private final Instant time = Instant.now();
    private final Integer version = 3;
    private final String parentId = "1b6fdea9-36b1-439e-bb1e-669e31109d56";
    private final EventPayload payload = new EventPayload();
    private final Event<EventPayload> resultingEvent = new Event<>();

    @BeforeEach
    void preparationOfData() {
        String id = "88e2a5b1-5041-4012-9b16-01fed1450018";
        resultingEvent.setId(id);
        resultingEvent.setEntityId(entityId);
        resultingEvent.setAuthor(authorId);
        String context = "${spring.application.name}";
        resultingEvent.setContext(context);
        resultingEvent.setTime(time);
        resultingEvent.setVersion(version);
        resultingEvent.setParentId(parentId);
        resultingEvent.setPayload(payload);
    }

    @Autowired
    private EventUtil eventUtil;

    @Test
    void uuidStringToLongShouldCorrectWork() {
        assertEquals(8315898343772737108L, EventUtil.uuidStringToLong(authorId));
    }

    @Test
    void populatedEventFieldsShouldCorrectWork1() {
        Event<EventPayload> event = new Event<>();
        eventUtil.populateEventFields(event, entityId, version, authorId, payload, false, parentId);
        event.setTime(resultingEvent.getTime());
        assertEquals(resultingEvent.getParentId(), event.getParentId());
        assertForEvent(resultingEvent, event);
    }

    @Test
    void populatedEventFieldsShouldCorrectWork2() {
        Event<EventPayload> event = new Event<>();
        eventUtil.populateEventFields(event, entityId, version, authorId, payload, true, parentId);
        event.setTime(resultingEvent.getTime());
        resultingEvent.setId(event.getId());
        assertEquals(resultingEvent.getParentId(), event.getParentId());
        assertForEvent(resultingEvent, event);
    }

    @Test
    void populatedEventFieldsShouldCorrectWork3() {
        Event<EventPayload> event = new Event<>();
        eventUtil.populateEventFields(event, entityId, version, authorId, payload, parentId);
        event.setTime(resultingEvent.getTime());
        resultingEvent.setId(event.getId());
        assertEquals(resultingEvent.getParentId(), event.getParentId());
        assertForEvent(resultingEvent, event);
    }

    @Test
    void populatedEventFieldsShouldCorrectWork4() {
        Event<EventPayload> event = new Event<>();
        eventUtil.populateEventFields(event, entityId, version, authorId, payload, true);
        event.setTime(resultingEvent.getTime());
        resultingEvent.setId(event.getId());
        assertNull(event.getParentId());
        assertForEvent(resultingEvent, event);
    }

    @Test
    void populatedEventFieldsShouldCorrectWork5() {
        Event<EventPayload> event = new Event<>();
        eventUtil.populateEventFields(event, entityId, version, authorId, payload);
        event.setTime(resultingEvent.getTime());
        resultingEvent.setId(event.getId());
        assertNull(event.getParentId());
        assertForEvent(resultingEvent, event);
    }

    private void assertForEvent(Event<EventPayload> e1, Event<EventPayload> e2) {
        assertEquals(e1.getEntityId(), e2.getEntityId());
        assertEquals(e1.getAuthor(), e2.getAuthor());
        assertEquals(e1.getContext(), e2.getContext());
        assertTrue(Objects.nonNull(e1.getTime()));
        assertTrue(Objects.nonNull(e2.getTime()));
        assertEquals(e1.getVersion(), e2.getVersion());
        assertEquals(e1.getPayload(), e2.getPayload());
    }
}
