package com.rm.toolkit.comment.command.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CommentNotFoundException extends ResponseStatusException {

    public CommentNotFoundException(String commentId) {
        super(HttpStatus.NOT_FOUND, commentId);
    }

}
