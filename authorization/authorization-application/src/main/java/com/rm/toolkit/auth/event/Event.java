package com.rm.toolkit.auth.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.auth.event.auth.UserMaxLoginAttemptsReachedEvent;
import com.rm.toolkit.auth.event.role.RoleCreatedEvent;
import com.rm.toolkit.auth.event.role.RoleDeletedEvent;
import com.rm.toolkit.auth.event.role.RoleEditedEvent;
import com.rm.toolkit.auth.event.user.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = EventType.class,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserMaxLoginAttemptsReachedEvent.class, name = "USER_MAX_LOGIN_ATTEMPTS_REACHED"),
        @JsonSubTypes.Type(value = UserBlockedEvent.class, name = "USER_BLOCKED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserRoleChangedEvent.class, name = "USER_ROLE_CHANGED"),
        @JsonSubTypes.Type(value = UserStatusChangedEvent.class, name = "USER_STATUS_CHANGED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = RoleCreatedEvent.class, name = "ROLE_CREATED"),
        @JsonSubTypes.Type(value = RoleEditedEvent.class, name = "ROLE_EDITED"),
        @JsonSubTypes.Type(value = RoleDeletedEvent.class, name = "ROLE_DELETED")
})
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@SuperBuilder
public class Event<T extends EventPayload> {

    /**
     * Идентификатор события
     */
    @Id
    protected String id;

    /**
     * Тип события
     */
    @Column(nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected EventType type;

    /**
     * Id объекта
     */
    @Column
    protected String entityId;

    /**
     * Id пользователя, создавшего событие
     */
    @Column
    protected String author;

    /**
     * Контекст события
     */
    @Column
    protected String context;

    /**
     * Время события
     */
    @Column
    protected Instant time;

    /**
     * Версия
     */
    @Column
    protected Integer version;

    /**
     * Если поле заполнено, значит событие возникло не само, а является каскадным
     */
    @Column
    protected String parentId;

    /**
     * Объект
     */
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
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