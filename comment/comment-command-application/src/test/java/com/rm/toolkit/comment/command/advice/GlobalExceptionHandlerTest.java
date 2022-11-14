package com.rm.toolkit.comment.command.advice;

import com.rm.toolkit.comment.command.advise.GlobalExceptionHandler;
import com.rm.toolkit.comment.command.dto.response.error.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    @Test
    void handleAllUnauthorizedExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        assertEquals(UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN,
                globalExceptionHandler.handleAllUnauthorizedExceptions(new ResponseStatusException(HttpStatus.CONTINUE))
                        .getErrorType());
    }

    @Test
    void handleAllForbiddenExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        assertEquals(ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS,
                globalExceptionHandler.handleAllForbiddenExceptions(new AccessDeniedException("Message"))
                        .getErrorType());
    }

    @Test
    void handleAllNotFoundExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<NotFoundErrorResponse> notFoundErrorResponseResponseEntity = globalExceptionHandler
                .handleAllNotFoundExceptions(new ResponseStatusException(HttpStatus.CONTINUE));
        assertTrue(notFoundErrorResponseResponseEntity.hasBody());
        assertTrue(notFoundErrorResponseResponseEntity.getHeaders().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, notFoundErrorResponseResponseEntity.getStatusCode());
    }


    @Test
    void handleBadRequestExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<BadRequestErrorResponse> badRequestErrorResponseResponseEntity = globalExceptionHandler
                .handleBadRequestExceptions(new ResponseStatusException(HttpStatus.CONTINUE));
        assertTrue(badRequestErrorResponseResponseEntity.hasBody());
        assertTrue(badRequestErrorResponseResponseEntity.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, badRequestErrorResponseResponseEntity.getStatusCode());
    }

    @Test
    void handleAllExceptionsWithStackTrace() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<InternalServerErrorResponse> internalServerErrorResponseResponseEntity = globalExceptionHandler
                .handleAllExceptionsWithStackTrace(new ResponseStatusException(HttpStatus.CONTINUE));
        assertTrue(internalServerErrorResponseResponseEntity.hasBody());
        assertTrue(internalServerErrorResponseResponseEntity.getHeaders().isEmpty());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, internalServerErrorResponseResponseEntity.getStatusCode());
    }
}