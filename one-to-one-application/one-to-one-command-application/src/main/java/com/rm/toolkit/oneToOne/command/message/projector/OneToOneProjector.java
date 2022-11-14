package com.rm.toolkit.oneToOne.command.message.projector;

import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.command.util.ProjectionUtil;
import com.rm.toolkit.oneToOne.command.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.oneToOne.command.model.OneToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class OneToOneProjector {

    private final ProjectionUtil projectionUtil;

    public OneToOne project(OneToOneCreatedEvent event) {
        var payload = event.getPayload();

        return OneToOne.builder()
                .id(event.getEntityId())
                .userId(payload.getUserId())
                .resourceManagerId(payload.getResourceManagerId())
                .dateTime(payload.getDateTime())
                .comment(payload.getComment())
                .isOver(payload.isOver())
                .isDeleted(payload.isDeleted())
                .version(1)
                .build();
    }

    public void project(OneToOneUpdatedEvent event, OneToOne oneToOne) {
        projectionUtil.validateEvent(event, oneToOne.getId(), oneToOne.getVersion());

        var payload = event.getPayload();

        oneToOne.setUserId(payload.getUserId());
        oneToOne.setResourceManagerId(payload.getResourceManagerId());
        oneToOne.setDateTime(payload.getDateTime());
        oneToOne.setComment(payload.getComment());
        oneToOne.setOver(payload.isOver());
        projectionUtil.incrementVersion(oneToOne);
    }

    public void project(OneToOneDeletedEvent event, OneToOne oneToOne) {
        projectionUtil.validateEvent(event, oneToOne.getId(), oneToOne.getVersion());

        oneToOne.setDeleted(true);
        projectionUtil.incrementVersion(oneToOne);
    }

    public void project(OneToOneCompletedEvent event, OneToOne oneToOne) {
        projectionUtil.validateEvent(event, oneToOne.getId(), oneToOne.getVersion());

        oneToOne.setOver(true);
        projectionUtil.incrementVersion(oneToOne);
    }
}
