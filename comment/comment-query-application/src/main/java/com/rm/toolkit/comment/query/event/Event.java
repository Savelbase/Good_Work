package com.rm.toolkit.comment.query.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.comment.query.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentEditedEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = EventType.class,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommentCreatedEvent.class, name = "COMMENT_CREATED"),
        @JsonSubTypes.Type(value = CommentEditedEvent.class, name = "COMMENT_UPDATED"),
        @JsonSubTypes.Type(value = CommentDeletedEvent.class, name = "COMMENT_DELETED")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id")
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
