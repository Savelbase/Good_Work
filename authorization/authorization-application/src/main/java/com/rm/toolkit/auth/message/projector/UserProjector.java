package com.rm.toolkit.auth.message.projector;

import com.rm.toolkit.auth.event.user.*;
import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.model.type.StatusType;
import com.rm.toolkit.auth.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProjector {

    private final ProjectionUtil projectionUtil;

    public User project(UserCreatedEvent event) {
        var payload = event.getPayload();

        return User.builder()
                .id(event.getEntityId())
                .email(payload.getEmail())
                .roleId(payload.getRoleId())
                .loginAttempts(0)
                .status(StatusType.ACTIVE)
                .version(1)
                .build();
    }

    public void project(UserEditedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setEmail(payload.getEmail());
        user.setRoleId(payload.getRoleId());
        user.setStatus(payload.getStatus());
        projectionUtil.incrementVersion(user);
    }

    public void project(UserStatusChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        user.setStatus(event.getPayload().getStatus());
        projectionUtil.incrementVersion(user);
    }

    public void project(UserBlockedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        user.setStatus(StatusType.BLOCKED);
        projectionUtil.incrementVersion(user);
    }

    public void project(UserRoleChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setRoleId(payload.getRoleId());
        projectionUtil.incrementVersion(user);
    }
}
