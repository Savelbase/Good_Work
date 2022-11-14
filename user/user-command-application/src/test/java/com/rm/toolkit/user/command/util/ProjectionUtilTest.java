package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.exception.ProjectionException;
import com.rm.toolkit.user.command.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {ProjectionUtil.class})
@ExtendWith(SpringExtension.class)
class ProjectionUtilTest {
    @Autowired
    private ProjectionUtil projectionUtil;

    @Test
    void shouldDoNothingOnValidateEvent() {
        int entityCurrentVersion = 0;
        String entityId = "Test";
        Event<? extends EventPayload> event = new Event<>();
        event.setEntityId("Test");
        event.setVersion(entityCurrentVersion + 1);

        assertDoesNotThrow(() -> projectionUtil.validateEvent(event, entityId, entityCurrentVersion));
    }

    @Test
    void shouldThrowProjectionExceptionOnDifferentIds() {
        int entityCurrentVersion = 0;
        String entityId = "Random Id";
        Event<? extends EventPayload> event = new Event<>();
        event.setEntityId("Test");
        event.setVersion(entityCurrentVersion + 1);

        assertThrows(ProjectionException.class, () -> projectionUtil.validateEvent(event, entityId, entityCurrentVersion));
    }

    @Test
    void shouldThrowProjectionExceptionOnDifferentVersions() {
        Integer entityCurrentVersion = 0;
        String entityId = "Test";
        Event<? extends EventPayload> event = new Event<>();
        event.setEntityId("Test");
        event.setVersion(9);

        assertThrows(ProjectionException.class, () -> projectionUtil.validateEvent(event, entityId, entityCurrentVersion));
    }

    @Test
    void incrementVersion() {
        Integer oldVersion = 0;
        Integer newVersion = 1;
        User user = new User();
        user.setVersion(oldVersion);

        projectionUtil.incrementVersion(user);

        assertEquals(newVersion, user.getVersion());
    }
}