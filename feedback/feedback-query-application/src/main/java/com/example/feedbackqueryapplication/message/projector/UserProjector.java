package com.example.feedbackqueryapplication.message.projector;

import com.example.feedbackqueryapplication.event.user.UserCreatedEvent;
import com.example.feedbackqueryapplication.event.user.UserEditedEvent;
import com.example.feedbackqueryapplication.model.User;
import com.example.feedbackqueryapplication.model.type.Role;
import com.example.feedbackqueryapplication.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProjector {

    private static final Integer VERSION_VALUE = 1;

    private final ProjectionUtil projectionUtil;

    public User project(UserCreatedEvent event){
        var payload = event.getPayload();

        return User.builder()
                .id(event.getEntityId())
                .role(Role.valueOf(payload.getRoleName().toUpperCase()))
                .version(VERSION_VALUE)
                .build();
    }

    public void project(UserEditedEvent event, User user) {
        projectionUtil.validateEvent(event, user.getId(), user.getVersion());

        var payload = event.getPayload();

        user.setRole(Role.valueOf(payload.getRoleName().toUpperCase()));
        projectionUtil.incrementVersion(user);
    }
}
