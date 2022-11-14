package com.rm.toolkit.feedbackcommandapplication.message;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.ChangeUsersDepartmentEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserEditedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        switch (event.getType()) {
            case USER_CREATED:
                eventHandler.handle((UserCreatedEvent) event);
                break;
            case USER_EDITED:
                eventHandler.handle((UserEditedEvent) event);
                break;
            case USER_DEPARTMENT_CHANGED:
                eventHandler.handle((UserDepartmentChangedEvent) event);
                break;
            case CHANGE_USERS_DEPARTMENT:
                eventHandler.handle((ChangeUsersDepartmentEvent) event);
                break;
            case DEPARTMENT_CREATED:
                eventHandler.handle((DepartmentCreatedEvent) event);
                break;
            case DEPARTMENT_EDITED:
                eventHandler.handle((DepartmentEditedEvent) event);
                break;
            case DEPARTMENT_DELETED:
                eventHandler.handle((DepartmentDeletedEvent) event);
                break;
            case ONE_TO_ONE_CREATED:
                eventHandler.handle((OneToOneCreatedEvent) event);
                break;
            case ONE_TO_ONE_UPDATED:
                eventHandler.handle((OneToOneUpdatedEvent) event);
                break;
            case ONE_TO_ONE_COMPLETED:
                eventHandler.handle((OneToOneCompletedEvent) event);
                break;
            case ONE_TO_ONE_DELETED:
                eventHandler.handle((OneToOneDeletedEvent) event);
                break;
        }
    }
}
