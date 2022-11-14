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
@DiscriminatorValue("USER_RM_CHANGED")
public class UserRmChangedEvent extends Event<UserRmChangedEvent.Payload> {

    public UserRmChangedEvent() {
        this.type = EventType.USER_RM_CHANGED;
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
