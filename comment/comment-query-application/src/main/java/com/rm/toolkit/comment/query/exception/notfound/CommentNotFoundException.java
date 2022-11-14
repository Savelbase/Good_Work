package com.rm.toolkit.comment.query.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CommentNotFoundException extends ResponseStatusException {

    public CommentNotFoundException(String commentId) {
        super(HttpStatus.NOT_FOUND, String.format("Комментария с id %s не существует", commentId));
    }

}

