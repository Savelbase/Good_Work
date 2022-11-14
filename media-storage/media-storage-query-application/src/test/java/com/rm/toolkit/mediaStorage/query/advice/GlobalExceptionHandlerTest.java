package com.rm.toolkit.mediaStorage.query.advice;

import com.rm.toolkit.mediaStorage.query.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.mediaStorage.query.dto.response.error.InternalServerErrorResponse;
import com.rm.toolkit.mediaStorage.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.mediaStorage.query.dto.response.error.UnauthorizedErrorResponse;
import com.rm.toolkit.mediaStorage.query.exception.FileReadException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

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
        ResponseEntity<NotFoundErrorResponse> actualHandleAllNotFoundExceptionsResult = globalExceptionHandler
                .handleAllNotFoundExceptions(new ResponseStatusException(HttpStatus.CONTINUE));
        assertTrue(actualHandleAllNotFoundExceptionsResult.hasBody());
        assertTrue(actualHandleAllNotFoundExceptionsResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.NOT_FOUND, actualHandleAllNotFoundExceptionsResult.getStatusCode());
    }

    @Test
    void handleFileReadException() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<InternalServerErrorResponse> fileReadExceptionResponseEntity = globalExceptionHandler
                .handleFileReadException(new ResponseStatusException(HttpStatus.CONTINUE));
        assertTrue(fileReadExceptionResponseEntity.hasBody());
        assertTrue(fileReadExceptionResponseEntity.getHeaders().isEmpty());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, fileReadExceptionResponseEntity.getStatusCode());
    }

    @Test
    void handleAllExceptionsWithStackTrace() {
        String errorMessage = "An error occur";
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<InternalServerErrorResponse> internalServerErrorResponseResponseEntity = globalExceptionHandler
                .handleAllExceptionsWithStackTrace(new Exception(errorMessage));
        assertTrue(internalServerErrorResponseResponseEntity.hasBody());
        assertTrue(internalServerErrorResponseResponseEntity.getHeaders().isEmpty());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, internalServerErrorResponseResponseEntity.getStatusCode());
        assertEquals(errorMessage, internalServerErrorResponseResponseEntity.getBody().getMessage());
    }
}