package com.rm.toolkit.mediaStorage.command.event.user;

import com.rm.toolkit.mediaStorage.command.event.Event;
import com.rm.toolkit.mediaStorage.command.event.EventPayload;
import com.rm.toolkit.mediaStorage.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
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
