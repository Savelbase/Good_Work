package com.rm.toolkit.user.query.event.user;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
