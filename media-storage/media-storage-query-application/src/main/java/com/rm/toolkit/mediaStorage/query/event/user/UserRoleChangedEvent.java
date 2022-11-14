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
@DiscriminatorValue("USER_ROLE_CHANGED")
public class UserRoleChangedEvent extends Event<UserRoleChangedEvent.Payload> {

    public UserRoleChangedEvent() {
        this.type = EventType.USER_ROLE_CHANGED;
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
