package com.rm.toolkit.oneToOne.query.event.user;

import com.rm.toolkit.oneToOne.query.event.Event;
import com.rm.toolkit.oneToOne.query.event.EventPayload;
import com.rm.toolkit.oneToOne.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserCreatedEvent extends Event<UserCreatedEvent.Payload> {

    public UserCreatedEvent() {
        this.type = EventType.USER_CREATED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {

        protected String firstName;

        protected String lastName;

        protected String resourceManagerId;

        protected Boolean isRm;
    }
}
