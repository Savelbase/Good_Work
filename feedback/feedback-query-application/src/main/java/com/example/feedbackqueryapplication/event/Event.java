package com.example.feedbackqueryapplication.event;

import com.example.feedbackqueryapplication.event.feedback.FeedbackCreatedEvent;
import com.example.feedbackqueryapplication.event.feedback.FeedbackEditedEvent;
import com.example.feedbackqueryapplication.event.user.UserCreatedEvent;
import com.example.feedbackqueryapplication.event.user.UserEditedEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
        @JsonSubTypes.Type(value = FeedbackCreatedEvent.class, name = "FEEDBACK_CREATED"),
        @JsonSubTypes.Type(value = FeedbackEditedEvent.class, name = "FEEDBACK_UPDATED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.class, name = "USER_EDITED"),
})
@Data
@NoArgsConstructor
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