package com.rm.toolkit.oneToOne.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.oneToOne.query.event.user.UserCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.user.UserEditedEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.oneToOne.query.event.oneToOne.OneToOneUpdatedEvent;

import javax.persistence.Column;
import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = EventType.class,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OneToOneCreatedEvent.class, name = "ONE_TO_ONE_CREATED"),
        @JsonSubTypes.Type(value = OneToOneUpdatedEvent.class, name = "ONE_TO_ONE_UPDATED"),
        @JsonSubTypes.Type(value = OneToOneDeletedEvent.class, name = "ONE_TO_ONE_DELETED"),
        @JsonSubTypes.Type(value = OneToOneCompletedEvent.class, name = "ONE_TO_ONE_COMPLETED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.class, name = "USER_EDITED"),
})
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