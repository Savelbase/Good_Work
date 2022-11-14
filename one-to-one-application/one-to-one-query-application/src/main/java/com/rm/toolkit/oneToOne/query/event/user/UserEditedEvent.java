package com.rm.toolkit.oneToOne.query.event.user;

import com.rm.toolkit.oneToOne.query.event.Event;
import com.rm.toolkit.oneToOne.query.event.EventPayload;
import com.rm.toolkit.oneToOne.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserEditedEvent extends Event<UserEditedEvent.Payload> {

    public UserEditedEvent() {
        this.type = EventType.USER_EDITED;
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
    }
}

