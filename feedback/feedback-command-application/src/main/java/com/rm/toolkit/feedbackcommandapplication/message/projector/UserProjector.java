package com.rm.toolkit.feedbackcommandapplication.message.projector;

import com.rm.toolkit.feedbackcommandapplication.event.user.ChangeUsersDepartmentEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.model.User;
import com.rm.toolkit.feedbackcommandapplication.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProjector {

    private static final Integer VERSION_VALUE = 1;

    private final ProjectionUtil projectionUtil;

    public User project(UserCreatedEvent event) {
        var payload = event.getPayload();

        return User.builder()
                .id(event.getEntityId())
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .email(payload.getEmail())
                .resourceManagerId(payload.getResourceManagerId())
                .departmentId(payload.getResourceManagerId())
                .version(VERSION_VALUE)
                .build();
    }

    public void project(UserEditedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setEmail(payload.getEmail());
        user.setResourceManagerId(payload.getResourceManagerId());
        user.setDepartmentId(payload.getDepartmentId());
        projectionUtil.incrementVersion(user);
    }

    public void project(UserDepartmentChangedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setResourceManagerId(payload.getResourceManagerId());
        user.setDepartmentId(payload.getDepartmentId());
        projectionUtil.incrementVersion(user);
    }

    public void project(ChangeUsersDepartmentEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setResourceManagerId(payload.getResourceManagerId());
        user.setDepartmentId(payload.getDepartmentId());
        projectionUtil.incrementVersion(user);
    }
}
