package com.rm.toolkit.comment.query.message;

import com.rm.toolkit.comment.query.event.Event;
import com.rm.toolkit.comment.query.event.EventPayload;
import com.rm.toolkit.comment.query.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentEditedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventGateway {

    private final EventHandler eventHandler;

    public void handle(Event<? extends EventPayload> event) {
        switch (event.getType()) {
            case COMMENT_CREATED:
                eventHandler.handle((CommentCreatedEvent) event);
                break;
            case COMMENT_UPDATED:
                eventHandler.handle((CommentEditedEvent) event);
                break;
            case COMMENT_DELETED:
                eventHandler.handle((CommentDeletedEvent) event);
                break;
        }
    }
}
