package com.rm.toolkit.feedbackcommandapplication.event.user;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.EventType;
import com.rm.toolkit.feedbackcommandapplication.model.type.StatusType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserEditedEvent extends Event<UserEditedEvent.Payload> {

    public UserEditedEvent() {
        this.type = EventType.USER_EDITED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
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

        protected StatusType status;

        protected Boolean isRm;
    }
}

