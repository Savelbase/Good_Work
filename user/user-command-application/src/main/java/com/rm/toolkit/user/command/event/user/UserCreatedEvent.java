package com.rm.toolkit.user.command.event.user;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.EventType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("USER_CREATED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserCreatedEvent extends Event<UserCreatedEvent.Payload> {

    public UserCreatedEvent() {
        this.type = EventType.USER_CREATED;
    }

    @AllArgsConstructor
    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Payload extends EventPayload {
        protected String firstName;

        protected String lastName;

        protected String email;

        protected String avatarPath;

        protected String resourceManagerId;

        protected String resourceManagerFirstName;

        protected String resourceManagerLastName;

        protected String departmentId;

        protected String departmentName;

        protected String countryId;

        protected String countryName;

        protected String cityId;

        protected String cityName;

        protected String roleId;

        protected String roleName;

        protected Map<String, String> activities;

        protected Boolean isRm;
    }
}
