package com.rm.toolkit.comment.command.message.projector;

import com.rm.toolkit.comment.command.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.command.event.comment.CommentEditedEvent;
import com.rm.toolkit.comment.command.model.Comment;
import com.rm.toolkit.comment.command.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentProjector {
    private final ProjectionUtil projectionUtil ;

    public Comment project(CommentCreatedEvent event){
        var payload = event.getPayload();
        return Comment.builder().id(event.getEntityId())
                .dateTime(ZonedDateTime.now())
                .userId(payload.getUserID())
                .senderId(payload.getSenderId())
                .text(payload.getText())
                .version(0)
                .deleted(false)
                .build();
    }

    public void project(CommentEditedEvent event , Comment comment){
        projectionUtil.validateEvent(event , comment.getId() , comment.getVersion());
        var payload = event.getPayload();
        comment.setText(payload.getText());
    }

}
