package com.rm.toolkit.mediaStorage.query.event.user;

import com.rm.toolkit.mediaStorage.query.event.Event;
import com.rm.toolkit.mediaStorage.query.event.EventPayload;
import com.rm.toolkit.mediaStorage.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@DiscriminatorValue("USER_BLOCKED")
public class UserBlockedEvent extends Event<UserBlockedEvent.Payload> {

    public UserBlockedEvent() {
        this.type = EventType.USER_BLOCKED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {

        protected String avatarPath;

    }
}
