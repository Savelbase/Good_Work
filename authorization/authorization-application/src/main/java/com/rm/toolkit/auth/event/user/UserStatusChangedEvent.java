package com.rm.toolkit.auth.event.user;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.EventType;
import com.rm.toolkit.auth.model.type.StatusType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("USER_STATUS_CHANGED")
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
