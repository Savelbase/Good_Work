package com.rm.toolkit.user.command.message.projector;

import com.rm.toolkit.user.command.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.command.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.command.event.role.RoleEditedEvent;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleProjector {

    private final ProjectionUtil projectionUtil;

    public Role project(RoleCreatedEvent event) {
        var payload = event.getPayload();

        return Role.builder()
                .id(event.getEntityId())
                .name(payload.getName())
                .priority(payload.getPriority())
                .authorities(payload.getAuthorities().toArray(AuthorityType[]::new))
                .version(1)
                .build();
    }

    public void project(RoleEditedEvent event, Role role) {
        projectionUtil.validateEvent(event, role.getId(), role.getVersion());

        var payload = event.getPayload();

        role.setName(payload.getName());
        role.setPriority(payload.getPriority());
        role.setAuthorities(payload.getAuthorities().toArray(AuthorityType[]::new));
        projectionUtil.incrementVersion(role);
    }

    public void project(RoleDeletedEvent event, Role role) {
        projectionUtil.validateEvent(event, role.getId(), role.getVersion());

        role.setDeleted(true);
        projectionUtil.incrementVersion(role);
    }
}
