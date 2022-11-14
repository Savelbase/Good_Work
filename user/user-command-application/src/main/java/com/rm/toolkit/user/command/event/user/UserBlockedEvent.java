package com.rm.toolkit.user.command.event.user;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("USER_BLOCKED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserBlockedEvent extends Event<UserBlockedEvent.Payload> {

    public UserBlockedEvent() {
        this.type = EventType.USER_BLOCKED;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
    }
}
