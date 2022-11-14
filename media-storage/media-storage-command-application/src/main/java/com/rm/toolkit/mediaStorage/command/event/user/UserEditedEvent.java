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
@DiscriminatorValue("USER_EDITED")
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
        protected String avatarPath;
    }
}
