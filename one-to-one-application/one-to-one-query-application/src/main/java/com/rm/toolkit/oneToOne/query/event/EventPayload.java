package com.rm.toolkit.oneToOne.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserEditedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OneToOneCreatedEvent.Payload.class, name = "ONE_TO_ONE_CREATED"),
        @JsonSubTypes.Type(value = OneToOneUpdatedEvent.Payload.class, name = "ONE_TO_ONE_UPDATED"),
        @JsonSubTypes.Type(value = OneToOneDeletedEvent.Payload.class, name = "ONE_TO_ONE_DELETED"),
        @JsonSubTypes.Type(value = OneToOneCompletedEvent.Payload.class, name = "ONE_TO_ONE_COMPLETED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.Payload.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.Payload.class, name = "USER_EDITED"),
})
public  class EventPayload {
    protected EventType type;
}