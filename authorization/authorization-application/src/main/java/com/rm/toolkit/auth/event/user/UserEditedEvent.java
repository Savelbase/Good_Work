package com.rm.toolkit.auth.event.user;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.EventType;
import com.rm.toolkit.auth.model.type.StatusType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("USER_EDITED")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserEditedEvent extends Event<UserEditedEvent.Payload> {

    public UserEditedEvent() {
        this.type = EventType.USER_EDITED;
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

        protected List<String> activitiesIds;

        protected StatusType status;

        protected boolean isRm;
    }
}

