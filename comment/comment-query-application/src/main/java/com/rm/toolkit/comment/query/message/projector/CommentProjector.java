package com.rm.toolkit.comment.query.message.projector;

import com.rm.toolkit.comment.query.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentEditedEvent;
import com.rm.toolkit.comment.query.model.Comment;
import com.rm.toolkit.comment.query.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentProjector {

    private final ProjectionUtil projectionUtil ;

    public Comment project(CommentCreatedEvent event) {
        var payload = event.getPayload();
        return Comment.builder().id(event.getEntityId())
                .dateTime(ZonedDateTime.now())
                .userId(payload.getUserID())
                .senderId(payload.getSenderId())
                .text(payload.getText())
                .version(0)
                .build();
    }

    public void project(CommentEditedEvent event , Comment comment) {
        projectionUtil.validateEvent(event , comment.getId() , comment.getVersion());

        var payload = event.getPayload();
        comment.setText(payload.getText());
    }

    public void project(CommentDeletedEvent event , Comment comment) {
        projectionUtil.validateEvent(event , comment.getId() , comment.getVersion());

        comment.setDeleted(true);
    }

}
