package com.rm.toolkit.comment.query.message;

import com.rm.toolkit.comment.query.event.Event;
import com.rm.toolkit.comment.query.event.EventPayload;
import com.rm.toolkit.comment.query.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.query.event.comment.CommentEditedEvent;
import com.rm.toolkit.comment.query.exception.notfound.CommentNotFoundException;
import com.rm.toolkit.comment.query.message.projector.CommentProjector;
import com.rm.toolkit.comment.query.model.Comment;
import com.rm.toolkit.comment.query.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final CommentRepository commentRepository;
    private final CommentProjector commentProjector;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(CommentCreatedEvent event) {
        Comment comment = commentProjector.project(event);
        commentRepository.save(comment);

        log.info("Комментарий с id {} создан после получения события", comment.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(CommentEditedEvent event) {
        Comment comment = getCommentFromEvent(event);
        commentProjector.project(event, comment);
        commentRepository.save(comment);

        log.info("Комментарий с id {} отредактирован после получения события", comment.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(CommentDeletedEvent event) {
        Comment comment = getCommentFromEvent(event);
        commentProjector.project(event, comment);
        commentRepository.save(comment);

        log.info("Комментарий с id {} удален после получения события", comment.getId());
    }


    private Comment getCommentFromEvent(Event<? extends EventPayload> event) {
        String commentId = event.getEntityId();

        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.info("Комментарий с id {} не существует", commentId);
            throw new CommentNotFoundException("Комментарий не найден");
        });
    }

}
