package com.rm.toolkit.auth.event.user;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("USER_ROLE_CHANGED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
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
        protected String roleId;

        protected String roleName;

        protected Boolean isRm;
    }
}
