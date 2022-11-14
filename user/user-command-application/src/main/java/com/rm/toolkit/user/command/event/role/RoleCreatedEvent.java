package com.rm.toolkit.user.command.event.role;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import com.rm.toolkit.user.command.security.AuthorityType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@DiscriminatorValue("ROLE_CREATED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RoleCreatedEvent extends Event<RoleCreatedEvent.Payload> {

    public RoleCreatedEvent() {
        this.type = EventType.ROLE_CREATED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected String name;

        protected Integer priority;

        protected Set<AuthorityType> authorities;
    }
}
