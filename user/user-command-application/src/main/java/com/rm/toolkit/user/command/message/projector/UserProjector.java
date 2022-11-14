package com.rm.toolkit.user.command.message.projector;

import com.rm.toolkit.user.command.event.user.*;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.util.ProjectionUtil;
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
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .resourceManagerId(payload.getResourceManagerId())
                .status(StatusType.ACTIVE)
                .departmentId(event.getPayload().getDepartmentId())
                .roleId(payload.getRoleId())
                .version(1)
                .build();
    }

    public void project(UserEditedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setEmail(payload.getEmail());
        user.setResourceManagerId(payload.getResourceManagerId());
        user.setStatus(event.getPayload().getStatus());
        user.setRoleId(payload.getRoleId());
        user.setDepartmentId(payload.getDepartmentId());
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

    public void project(UserDepartmentChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setDepartmentId(payload.getDepartmentId());
        user.setResourceManagerId(payload.getResourceManagerId());
        projectionUtil.incrementVersion(user);
    }

    public void project(UserRmChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        user.setResourceManagerId(event.getPayload().getResourceManagerId());
        projectionUtil.incrementVersion(user);
    }

    public void project(UserRoleChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        user.setRoleId(event.getPayload().getRoleId());
        projectionUtil.incrementVersion(user);
    }

    public void project(ChangeUsersDepartmentEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setDepartmentId(payload.getDepartmentId());
        projectionUtil.incrementVersion(user);
    }

}
