package com.rm.toolkit.user.query.message.projector;

import com.rm.toolkit.user.query.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.query.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.query.event.role.RoleEditedEvent;
import com.rm.toolkit.user.query.model.Role;
import com.rm.toolkit.user.query.util.ProjectionUtil;
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
                .authorities(payload.getAuthorities())
                .version(1)
                .build();
    }

    public void project(RoleEditedEvent event, Role role) {
        projectionUtil.validateEvent(event, role.getId(), role.getVersion());

        var payload = event.getPayload();

        role.setName(payload.getName());
        role.setPriority(payload.getPriority());
        role.setAuthorities(payload.getAuthorities());
        projectionUtil.incrementVersion(role);
    }

    public void project(RoleDeletedEvent event, Role role) {
        projectionUtil.validateEvent(event, role.getId(), role.getVersion());

        role.setDeleted(true);
        projectionUtil.incrementVersion(role);
    }
}
