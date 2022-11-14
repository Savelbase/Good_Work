package com.rm.toolkit.auth.event.auth;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("USER_MAX_LOGIN_ATTEMPTS_REACHED")
public class UserMaxLoginAttemptsReachedEvent extends Event<UserMaxLoginAttemptsReachedEvent.Payload> {

    public UserMaxLoginAttemptsReachedEvent() {
        this.type = EventType.USER_MAX_LOGIN_ATTEMPTS_REACHED;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
    }
}
