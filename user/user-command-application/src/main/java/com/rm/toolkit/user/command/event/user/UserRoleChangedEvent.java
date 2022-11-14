package com.rm.toolkit.user.command.event.user;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

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
