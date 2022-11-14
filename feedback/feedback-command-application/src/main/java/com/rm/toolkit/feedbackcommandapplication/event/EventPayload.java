package com.rm.toolkit.feedbackcommandapplication.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.feedback.FeedbackCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.feedback.FeedbackEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.ChangeUsersDepartmentEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserEditedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FeedbackCreatedEvent.Payload.class, name = "FEEDBACK_CREATED"),
        @JsonSubTypes.Type(value = FeedbackEditedEvent.Payload.class, name = "FEEDBACK_UPDATED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.Payload.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.Payload.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.Payload.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = ChangeUsersDepartmentEvent.Payload.class, name = "CHANGE_USERS_DEPARTMENT"),
        @JsonSubTypes.Type(value = DepartmentCreatedEvent.Payload.class, name = "DEPARTMENT_CREATED"),
        @JsonSubTypes.Type(value = DepartmentEditedEvent.Payload.class, name = "DEPARTMENT_EDITED"),
        @JsonSubTypes.Type(value = DepartmentDeletedEvent.Payload.class, name = "DEPARTMENT_DELETED"),
        @JsonSubTypes.Type(value = OneToOneCreatedEvent.Payload.class, name = "ONE_TO_ONE_CREATED"),
        @JsonSubTypes.Type(value = OneToOneUpdatedEvent.Payload.class, name = "ONE_TO_ONE_UPDATED"),
        @JsonSubTypes.Type(value = OneToOneDeletedEvent.Payload.class, name = "ONE_TO_ONE_DELETED"),
        @JsonSubTypes.Type(value = OneToOneCompletedEvent.Payload.class, name = "ONE_TO_ONE_COMPLETED")
})
@Data
@NoArgsConstructor
public class EventPayload {
}
