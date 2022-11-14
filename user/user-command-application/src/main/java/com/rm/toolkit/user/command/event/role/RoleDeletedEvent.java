package com.rm.toolkit.user.command.event.role;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ROLE_DELETED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class RoleDeletedEvent extends Event<RoleDeletedEvent.Payload> {

    public RoleDeletedEvent() {
        this.type = EventType.ROLE_DELETED;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
    }
}
