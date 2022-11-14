package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.event.Event;
import com.rm.toolkit.oneToOne.command.event.EventPayload;
import com.rm.toolkit.oneToOne.command.event.EventType;
import com.rm.toolkit.oneToOne.command.exception.ProjectionException;
import com.rm.toolkit.oneToOne.command.model.iface.Versionable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;

@ContextConfiguration(classes = ProjectionUtil.class)
@ExtendWith(SpringExtension.class)
public class ProjectionUtilTest {

    @Autowired
    private ProjectionUtil projectionUtil;

    private final Event<EventPayload> event = new Event<>();
    private static final Instant startDateTime = ZonedDateTime.of(2020, 1, 1,
            0, 0, 0, 1, ZoneId.of("UTC")).toInstant();

    @BeforeEach
    void preparationOfData() {
        event.setId("idForTest");
        event.setAuthor("authorIdForTest");
        event.setContext("contextForTest");
        event.setEntityId("entityIdForTest");
        event.setParentId("parentIdForTest");
        event.setPayload(new EventPayload());
        event.setTime(startDateTime);
        event.setType(EventType.ONE_TO_ONE_CREATED);
        event.setVersion(1);
    }

    @Test
    void validateEventShouldNotThrowException() {
        Assertions.assertDoesNotThrow(
                () -> projectionUtil.validateEvent(event, event.getEntityId(), event.getVersion() - 1)
        );
    }

    @Test
    void validateEventShouldThrowException1() {
        Assertions.assertThrows(ProjectionException.class,
                () -> projectionUtil.validateEvent(event, event.getEntityId() + "a", event.getVersion() - 1));
    }

    @Test
    void validateEventShouldThrowException2() {
        Assertions.assertThrows(ProjectionException.class,
                () -> projectionUtil.validateEvent(event, event.getEntityId(), event.getVersion()));
    }

    @Test
    void incrementVersionShouldCorrectWork() {
        class SomeWithVersion implements Versionable {
            private Integer version = 2;

            @Override
            public Integer getVersion() {
                return version;
            }

            @Override
            public void setVersion(Integer version) {
                this.version = version;
            }
        }
        SomeWithVersion someWithVersion = new SomeWithVersion();
        Integer currentVersion = someWithVersion.getVersion();
        projectionUtil.incrementVersion(someWithVersion);
        Assertions.assertEquals(currentVersion + 1, (int) someWithVersion.getVersion());
    }
}
