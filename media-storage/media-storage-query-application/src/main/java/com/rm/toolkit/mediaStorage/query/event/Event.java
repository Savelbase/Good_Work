package com.rm.toolkit.mediaStorage.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarConfirmedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarDeletedEvent;
import com.rm.toolkit.mediaStorage.query.event.avatar.AvatarUploadedEvent;
import com.rm.toolkit.mediaStorage.query.event.user.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = EventType.class,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AvatarUploadedEvent.class, name = "AVATAR_UPLOADED"),
        @JsonSubTypes.Type(value = AvatarConfirmedEvent.class, name = "AVATAR_CONFIRMED"),
        @JsonSubTypes.Type(value = AvatarDeletedEvent.class, name = "AVATAR_DELETED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserBlockedEvent.class, name = "USER_BLOCKED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = UserRmChangedEvent.class, name = "USER_RM_CHANGED"),
        @JsonSubTypes.Type(value = UserRoleChangedEvent.class, name = "USER_ROLE_CHANGED"),
        @JsonSubTypes.Type(value = UserStatusChangedEvent.class, name = "USER_STATUS_CHANGED")
})
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@SuperBuilder
public class Event<T extends EventPayload> {

    /**
     * Идентификатор события
     */
    protected String id;

    /**
     * Тип события
     */
    protected EventType type;

    /**
     * Id объекта
     */
    protected String entityId;

    /**
     * Id пользователя, создавшего событие
     */
    protected String author;

    /**
     * Контекст события
     */
    protected String context;

    /**
     * Время события
     */
    protected Instant time;

    /**
     * Версия
     */
    protected Integer version;

    /**
     * Если поле заполнено, значит событие возникло не само, а является каскадным
     */
    @Column
    protected String parentId;


    /**
     * Объект
     */
    protected T payload;

    public Event(Event<T> event) {
        this.id = event.getId();
        this.type = event.getType();
        this.entityId = event.getEntityId();
        this.author = event.getAuthor();
        this.context = event.getContext();
        this.time = event.getTime();
        this.version = event.getVersion();
        this.parentId = event.getParentId();
        this.payload = event.getPayload();
    }
}