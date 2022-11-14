package com.rm.toolkit.feedbackcommandapplication.message.projector;

import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.feedbackcommandapplication.model.OneToOne;
import com.rm.toolkit.feedbackcommandapplication.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OneToOneProjector {

    private static final Integer VERSION_VALUE = 1;

    private final ProjectionUtil projectionUtil;

    public OneToOne project(OneToOneCreatedEvent event) {
        var payload = event.getPayload();

        return OneToOne.builder()
                .id(event.getEntityId())
                .userId(payload.getUserId())
                .resourceManagerId(payload.getResourceManagerId())
                .dateTime(payload.getDateTime())
                .isOver(false)
                .isDeleted(false)
                .version(VERSION_VALUE)
                .build();
    }

    public void project(OneToOneUpdatedEvent event, OneToOne oneToOne) {
        projectionUtil.validateEvent(event, oneToOne.getId(), oneToOne.getVersion());

        var payload = event.getPayload();

        oneToOne.setUserId(payload.getUserId());
        oneToOne.setResourceManagerId(payload.getResourceManagerId());
        oneToOne.setDateTime(payload.getDateTime());
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
