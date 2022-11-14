package com.rm.toolkit.mediaStorage.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.event.user.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AvatarUploadedEvent.Payload.class, name = "AVATAR_UPLOADED"),
        @JsonSubTypes.Type(value = AvatarConfirmedEvent.Payload.class, name = "AVATAR_CONFIRMED"),
        @JsonSubTypes.Type(value = AvatarDeletedEvent.Payload.class, name = "AVATAR_DELETED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.Payload.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.Payload.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserBlockedEvent.Payload.class, name = "USER_BLOCKED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.Payload.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = UserRmChangedEvent.Payload.class, name = "USER_RM_CHANGED"),
        @JsonSubTypes.Type(value = UserRoleChangedEvent.Payload.class, name = "USER_ROLE_CHANGED"),
        @JsonSubTypes.Type(value = UserStatusChangedEvent.Payload.class, name = "USER_STATUS_CHANGED")
})
@Data
@NoArgsConstructor
public class EventPayload {
    protected EventType type;
}