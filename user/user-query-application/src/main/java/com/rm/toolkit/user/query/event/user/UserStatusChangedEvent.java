package com.rm.toolkit.user.query.event.user;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.EventType;
import com.rm.toolkit.user.query.model.type.StatusType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserStatusChangedEvent extends Event<UserStatusChangedEvent.Payload> {

    public UserStatusChangedEvent() {
        this.type = EventType.USER_STATUS_CHANGED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected StatusType status;
    }
}
