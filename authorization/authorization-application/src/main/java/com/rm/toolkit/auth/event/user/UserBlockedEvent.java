package com.rm.toolkit.auth.event.user;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.EventType;
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
