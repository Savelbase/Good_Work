package com.rm.toolkit.comment.command.service;

import com.rm.toolkit.comment.command.dto.command.CreateCommentCommand;
import com.rm.toolkit.comment.command.dto.command.EditCommentCommand;
import com.rm.toolkit.comment.command.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.command.event.comment.CommentDeletedEvent;
import com.rm.toolkit.comment.command.event.comment.CommentEditedEvent;
import com.rm.toolkit.comment.command.exception.notfound.CommentNotFoundException;
import com.rm.toolkit.comment.command.message.EventPublisher;
import com.rm.toolkit.comment.command.message.projector.CommentProjector;
import com.rm.toolkit.comment.command.model.Comment;
import com.rm.toolkit.comment.command.repository.CommentRepository;
import com.rm.toolkit.comment.command.util.EventUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Log4j2
public class CommentCommandService {
    private static final Integer VERSION = 1 ;
    private final CommentRepository commentRepository ;
    private final CommentProjector commentProjector ;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;

    @Transactional
    public void addNewComment(CreateCommentCommand command , String authorId ){
        log.info("addNewComment method");
        CommentCreatedEvent.Payload payload = CommentCreatedEvent.Payload.builder()
                .dateTime(ZonedDateTime.now())
                .userID(command.getUserId())
                .senderId(command.getSenderId())
                .text(command.getText())
                .deleted(false)
                .build();

        CommentCreatedEvent event = new CommentCreatedEvent();
        event.setPayload(payload);
        eventUtil.populateEventFields(event , UUID.randomUUID().toString() , VERSION , authorId , payload , true);
        eventPublisher.publishNoReupload(event);
        Comment comment = commentProjector.project(event);
        commentRepository.save(comment);
    }

    @Transactional
    public void editComment(EditCommentCommand command , String commentId , String authorId){
        log.info("editComment method");
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()){
            throw new CommentNotFoundException("Комментарий не найден");
        }
        String commentEditedEventId = UUID.randomUUID().toString();
        CommentEditedEvent.Payload payload = CommentEditedEvent.Payload.builder()
                .userID(command.getUserId())
                .senderId(command.getSenderId())
                .dateTime(ZonedDateTime.now())
                .text(command.getText())
                .deleted(false)
                .build();
        CommentEditedEvent event = new CommentEditedEvent();
        event.setPayload(payload);
        event.setId(commentEditedEventId);
        event.setEntityId(commentId);
        event.setVersion(VERSION);
        commentProjector.project(event , comment.get());
        eventUtil.populateEventFields(event , command.getId() , VERSION , authorId , payload);
        commentRepository.save(comment.get());
        eventPublisher.publishWithReupload(event);
    }

    @Transactional
    public void deleteComment(String commentId , String authorId){
        log.info("deleteComment method");
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty())
            throw new CommentNotFoundException("Комментарий не найден");
        String commentDeletedEventId = UUID.randomUUID().toString();
        CommentDeletedEvent.Payload payload = CommentDeletedEvent.Payload.builder()
                .id(commentId)
                .dateTime(ZonedDateTime.now())
                .build();
        CommentDeletedEvent event = new CommentDeletedEvent();
        event.setPayload(payload);
        event.setId(commentDeletedEventId);
        event.setEntityId(commentId);
        eventUtil.populateEventFields(event , commentId , VERSION , authorId , payload , true);
        commentRepository.deleteById(comment.get().getId());
        eventPublisher.publishNoReupload(event);
    }
}
