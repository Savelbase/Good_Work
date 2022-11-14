package com.rm.toolkit.auth.event.role;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.EventType;
import com.rm.toolkit.auth.security.AuthorityType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ROLE_EDITED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RoleEditedEvent extends Event<RoleEditedEvent.Payload> {

    public RoleEditedEvent() {
        this.type = EventType.ROLE_EDITED;
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
