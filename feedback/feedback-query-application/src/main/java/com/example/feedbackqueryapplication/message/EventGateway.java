package com.example.feedbackqueryapplication.message;

import com.example.feedbackqueryapplication.event.Event;
import com.example.feedbackqueryapplication.event.EventPayload;
import com.example.feedbackqueryapplication.event.feedback.FeedbackCreatedEvent;
import com.example.feedbackqueryapplication.event.feedback.FeedbackEditedEvent;
import com.example.feedbackqueryapplication.event.user.UserCreatedEvent;
import com.example.feedbackqueryapplication.event.user.UserEditedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        switch (event.getType()) {
            case FEEDBACK_CREATED:
                eventHandler.handle((FeedbackCreatedEvent) event);
                break;
            case FEEDBACK_UPDATED:
                eventHandler.handle((FeedbackEditedEvent) event);
                break;
            case USER_CREATED:
                eventHandler.handle((UserCreatedEvent) event);
                break;
            case USER_EDITED:
                eventHandler.handle((UserEditedEvent) event);
                break;
        }
    }
}
