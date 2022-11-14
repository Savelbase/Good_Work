package com.rm.toolkit.oneToOne.command.message.projector;

import com.rm.toolkit.oneToOne.command.event.user.UserCreatedEvent;
import com.rm.toolkit.oneToOne.command.event.user.UserEditedEvent;
import com.rm.toolkit.oneToOne.command.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProjector {

    public User project(UserCreatedEvent event) {
        var payload = event.getPayload();

        return User.builder()
                .id(event.getEntityId())
                .firstName(payload.getFirstName())
                .lastName(payload.getLastName())
                .email(payload.getEmail())
                .resourceManagerId(payload.getResourceManagerId())
                .build();
    }

    public void project(UserEditedEvent event, User user) {
        var payload = event.getPayload();

        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());
        user.setEmail(payload.getEmail());
        user.setResourceManagerId(payload.getResourceManagerId());
    }

}

