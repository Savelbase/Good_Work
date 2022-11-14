package com.rm.toolkit.comment.command.service;

import com.rm.toolkit.comment.command.dto.command.CreateCommentCommand;
import com.rm.toolkit.comment.command.dto.command.EditCommentCommand;
import com.rm.toolkit.comment.command.event.comment.CommentCreatedEvent;
import com.rm.toolkit.comment.command.event.comment.CommentEditedEvent;
import com.rm.toolkit.comment.command.message.EventPublisher;
import com.rm.toolkit.comment.command.message.projector.CommentProjector;
import com.rm.toolkit.comment.command.model.Comment;
import com.rm.toolkit.comment.command.repository.CommentRepository;
import com.rm.toolkit.comment.command.testUtil.TestModelsBuilder;
import com.rm.toolkit.comment.command.util.EventUtil;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CommentCommandService.class, TestModelsBuilder.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(CommentProjector.class), @MockBean(CommentRepository.class),
        @MockBean(EventPublisher.class), @MockBean(EventUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentCommandServiceTest {

    private final CommentCommandService commentCommandService;
    private final TestModelsBuilder modelsBuilder;
    private final CommentRepository commentRepository;
    private final CommentProjector commentProjector;

    private final String userId = "Test";

    private final Supplier<CreateCommentCommand> createCommentCommandSupplier = () -> {
        String value = "Test";
        CreateCommentCommand command = new CreateCommentCommand();
        command.setId(value);
        command.setUserId(value);
        command.setText(value);
        command.setSenderId(value);
        command.setDateTime(ZonedDateTime.now());
        return command;
    };

    private final Supplier<EditCommentCommand> editCommentCommandSupplier = () -> {
        String value = "Test";
        EditCommentCommand command = new EditCommentCommand();
        command.setId(value);
        command.setUserId(value);
        command.setText(value);
        command.setSenderId(value);
        command.setDateTime(ZonedDateTime.now());
        return command;
    };

    @Test
    void createComment() {
        CreateCommentCommand command = createCommentCommandSupplier.get();
        Comment comment = modelsBuilder.getTestComment();
        when(commentProjector.project(any(CommentCreatedEvent.class))).thenReturn(comment);
        commentCommandService.addNewComment(command , userId);
        verify(commentProjector, times(1)).project(isA(CommentCreatedEvent.class));
        verify(commentRepository, times(1)).save(isA(Comment.class));
    }

    @Test
    void  editComment(){
        EditCommentCommand command = editCommentCommandSupplier.get();
        Comment comment = modelsBuilder.getTestComment();
        when(commentProjector.project(any())).thenReturn(comment);
        when(commentRepository.findById(anyString())).thenReturn(Optional.of(comment));
        commentCommandService.editComment(command , comment.getId() , userId);
        verify(commentRepository , times(1)).findById(anyString());
    }

    @Test
    void deleteComment(){
        Comment comment = modelsBuilder.getTestComment();
        when(commentRepository.findById(anyString())).thenReturn(Optional.of(comment));
        commentCommandService.deleteComment(anyString() , userId);
        verify(commentRepository , times(1)).findById(anyString());
        verify(commentRepository , times(1)).deleteById(anyString());

    }
}
