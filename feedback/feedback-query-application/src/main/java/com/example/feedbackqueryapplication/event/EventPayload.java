package com.example.feedbackqueryapplication.event;

import com.example.feedbackqueryapplication.event.feedback.FeedbackCreatedEvent;
import com.example.feedbackqueryapplication.event.feedback.FeedbackEditedEvent;
import com.example.feedbackqueryapplication.event.user.UserCreatedEvent;
import com.example.feedbackqueryapplication.event.user.UserEditedEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FeedbackCreatedEvent.Payload.class, name = "FEEDBACK_CREATED"),
        @JsonSubTypes.Type(value = FeedbackEditedEvent.Payload.class, name = "FEEDBACK_UPDATED"),
        @JsonSubTypes.Type(value = UserCreatedEvent.Payload.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserEditedEvent.Payload.class, name = "USER_EDITED"),
})
@Data
@NoArgsConstructor
public class EventPayload {
}
