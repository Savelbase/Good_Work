package com.rm.toolkit.feedbackcommandapplication.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.feedback.FeedbackCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.feedback.FeedbackEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.ChangeUsersDepartmentEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserEditedEvent;
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
        @JsonSubTypes.Type(value = FeedbackCreatedEvent.class, name = "FEEDBACK_CREATED"),
        @JsonSubTypes.Type(value = FeedbackEditedEvent.class, name = "FEEDBACK_UPDATED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.class, name = "USER_EDITED"),
        @JsonSubTypes.Type(value = UserDepartmentChangedEvent.class, name = "USER_DEPARTMENT_CHANGED"),
        @JsonSubTypes.Type(value = ChangeUsersDepartmentEvent.class, name = "CHANGE_USERS_DEPARTMENT"),
        @JsonSubTypes.Type(value = DepartmentCreatedEvent.class, name = "DEPARTMENT_CREATED"),
        @JsonSubTypes.Type(value = DepartmentEditedEvent.class, name = "DEPARTMENT_EDITED"),
        @JsonSubTypes.Type(value = DepartmentDeletedEvent.class, name = "DEPARTMENT_DELETED"),
        @JsonSubTypes.Type(value = OneToOneCreatedEvent.class, name = "ONE_TO_ONE_CREATED"),
        @JsonSubTypes.Type(value = OneToOneUpdatedEvent.class, name = "ONE_TO_ONE_UPDATED"),
        @JsonSubTypes.Type(value = OneToOneDeletedEvent.class, name = "ONE_TO_ONE_DELETED"),
        @JsonSubTypes.Type(value = OneToOneCompletedEvent.class, name = "ONE_TO_ONE_COMPLETED")
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