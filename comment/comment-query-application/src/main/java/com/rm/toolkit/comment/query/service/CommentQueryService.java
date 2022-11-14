package com.rm.toolkit.comment.query.service;

import com.rm.toolkit.comment.query.model.Comment;
import com.rm.toolkit.comment.query.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentQueryService {

    private static final String DATE_TIME_COMMENT_PARAMETER = "dateTime";

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, DATE_TIME_COMMENT_PARAMETER));

        return commentRepository.findAllCommentsByUserIdOrderByDateTimeDesc(userId, pageable)
                .orElseThrow();
    }
}
