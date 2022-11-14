package com.rm.toolkit.comment.query.service;


import com.rm.toolkit.comment.query.message.projector.CommentProjector;
import com.rm.toolkit.comment.query.model.Comment;
import com.rm.toolkit.comment.query.repository.CommentRepository;
import com.rm.toolkit.comment.query.testUtils.TestModelsBuilder;
import com.rm.toolkit.comment.query.util.EventUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CommentQueryService.class, TestModelsBuilder.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(CommentProjector.class), @MockBean(CommentRepository.class),
        @MockBean(EventUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentQueryServiceTest {
    private final CommentQueryService commentQueryService;
    private final TestModelsBuilder modelsBuilder;

    private final CommentRepository commentRepository;
    private final String userId = "Test";

    @Test
    void getCommentsByUserId() {
        Comment comment = modelsBuilder.getTestComment();
        when(commentRepository.findAllCommentsByUserIdOrderByDateTimeDesc(any() , any()))
                .thenReturn(Optional.of(List.of(new Comment[]{comment})));
        commentQueryService.getCommentsByUserId(userId , 0 , 10);
        verify(commentRepository, times(1))
                .findAllCommentsByUserIdOrderByDateTimeDesc(isA(String.class) , any());
    }

}
