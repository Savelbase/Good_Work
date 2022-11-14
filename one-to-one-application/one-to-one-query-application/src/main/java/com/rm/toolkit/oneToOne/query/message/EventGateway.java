package com.rm.toolkit.oneToOne.query.message;

import com.rm.toolkit.oneToOne.query.event.Event;
import com.rm.toolkit.oneToOne.query.event.EventPayload;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserEditedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        switch (event.getType()) {
            case ONE_TO_ONE_CREATED:
                eventHandler.handle((OneToOneCreatedEvent) event);
                break;
            case ONE_TO_ONE_UPDATED:
                eventHandler.handle((OneToOneUpdatedEvent) event);
                break;
            case ONE_TO_ONE_DELETED:
                eventHandler.handle((OneToOneDeletedEvent) event);
                break;
            case ONE_TO_ONE_COMPLETED:
                eventHandler.handle((OneToOneCompletedEvent) event);
                break;
            case USER_CREATED:
                eventHandler.handle((UserCreatedEvent) event);
                break;
            case USER_EDITED:
                eventHandler.handle((UserEditedEvent) event);
                break;
        }
    }
}
