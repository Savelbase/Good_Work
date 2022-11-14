package com.rm.toolkit.user.query.message.projector;

import com.rm.toolkit.user.query.event.user.ChangeUsersDepartmentEvent;
import com.rm.toolkit.user.query.event.user.*;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.model.embedded.*;
import com.rm.toolkit.user.query.model.type.StatusType;
import com.rm.toolkit.user.query.util.ProjectionUtil;
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
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .email(payload.getEmail())
                .avatarPath(payload.getAvatarPath())
                .resourceManager(new UserEmbedded(payload.getResourceManagerId(),
                        payload.getResourceManagerFirstName(), payload.getResourceManagerLastName()))
                .status(StatusType.ACTIVE)
                .department(new DepartmentEmbedded(payload.getDepartmentId(), payload.getDepartmentName()))
                .city(new CityEmbedded(payload.getCityId(), payload.getCityName()))
                .country(new CountryEmbedded(payload.getCountryId(), payload.getCountryName()))
                .role(new RoleEmbedded(payload.getRoleId(), payload.getRoleName()))
                .isRm(payload.getIsRm())
                .activities(projectionUtil.convertMapToActivitiesEmbedded(payload.getActivities()))
                .version(1)
                .build();
    }

    public void project(UserEditedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setEmail(payload.getEmail());
        user.setAvatarPath(payload.getAvatarPath());
        user.setResourceManager(new UserEmbedded(payload.getResourceManagerId(),
                payload.getResourceManagerFirstName(), payload.getResourceManagerLastName()));
        user.setStatus(payload.getStatus());
        user.setDepartment(new DepartmentEmbedded(payload.getDepartmentId(), payload.getDepartmentName()));
        user.setCity(new CityEmbedded(payload.getCityId(), payload.getCityName()));
        user.setCountry(new CountryEmbedded(payload.getCountryId(), payload.getCountryName()));
        user.setRole(new RoleEmbedded(payload.getRoleId(), payload.getRoleName()));
        user.setRm(payload.getIsRm());
        user.setActivities(projectionUtil.convertMapToActivitiesEmbedded(payload.getActivities()));
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

        user.setDepartment(new DepartmentEmbedded(payload.getDepartmentId(), payload.getDepartmentName()));
        user.setResourceManager(new UserEmbedded(payload.getResourceManagerId(),
                payload.getResourceManagerFirstName(), payload.getResourceManagerLastName()));
        projectionUtil.incrementVersion(user);
    }

    public void project(UserRmChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setResourceManager(new UserEmbedded(payload.getResourceManagerId(),
                payload.getResourceManagerFirstName(), payload.getResourceManagerLastName()));
        projectionUtil.incrementVersion(user);
    }

    public void project(UserRoleChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setRole(new RoleEmbedded(payload.getRoleId(), payload.getRoleName()));
        user.setRm(payload.getIsRm());
        projectionUtil.incrementVersion(user);
    }

    public void project(ChangeUsersDepartmentEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());
        var payload = event.getPayload();

        user.setDepartment(new DepartmentEmbedded(payload.getDepartmentId(), payload.getDepartmentName()));
        user.setResourceManager(new UserEmbedded(payload.getResourceManagerId(),
                payload.getResourceManagerFirstName(), payload.getResourceManagerLastName()));
        projectionUtil.incrementVersion(user);
    }

}
