package com.rm.toolkit.user.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.user.query.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.query.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.query.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.query.event.role.RoleEditedEvent;
import com.rm.toolkit.user.query.event.user.*;
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
        @JsonSubTypes.Type(value = UserBlockedEvent.class, name = "USER_BLOCKED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = UserRmChangedEvent.class, name = "USER_RM_CHANGED"),
        @JsonSubTypes.Type(value = UserRoleChangedEvent.class, name = "USER_ROLE_CHANGED"),
        @JsonSubTypes.Type(value = UserStatusChangedEvent.class, name = "USER_STATUS_CHANGED"),
        @JsonSubTypes.Type(value = ChangeUsersDepartmentEvent.class, name = "CHANGE_USERS_DEPARTMENT"),
        @JsonSubTypes.Type(value = RoleCreatedEvent.class, name = "ROLE_CREATED"),
        @JsonSubTypes.Type(value = RoleEditedEvent.class, name = "ROLE_EDITED"),
        @JsonSubTypes.Type(value = RoleDeletedEvent.class, name = "ROLE_DELETED"),
        @JsonSubTypes.Type(value = DepartmentCreatedEvent.class, name = "DEPARTMENT_CREATED"),
        @JsonSubTypes.Type(value = DepartmentEditedEvent.class, name = "DEPARTMENT_EDITED"),
        @JsonSubTypes.Type(value = DepartmentDeletedEvent.class, name = "DEPARTMENT_DELETED")
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