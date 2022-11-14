package com.rm.toolkit.user.query.event.role;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.EventType;
import com.rm.toolkit.user.query.security.AuthorityType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RoleCreatedEvent extends Event<RoleCreatedEvent.Payload> {

    public RoleCreatedEvent() {
        this.type = EventType.ROLE_CREATED;
    }

    public RoleCreatedEvent(Event<? extends EventPayload> event) {
        super((RoleCreatedEvent) event);
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected String name;

        protected Integer priority;

        protected AuthorityType[] authorities;
    }
}
