package com.rm.toolkit.mediaStorage.command.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rm.toolkit.mediaStorage.command.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.FileTooLargeErrorResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.InternalServerErrorResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.UnauthorizedErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {
    @Test
    void testHandleAllUnauthorizedExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        assertEquals(UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN,
                globalExceptionHandler.handleAllUnauthorizedExceptions(new ResponseStatusException(HttpStatus.CONTINUE))
                        .getErrorType());
    }

    @Test
    void testHandleAllForbiddenExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        assertEquals(ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS,
                globalExceptionHandler.handleAllForbiddenExceptions(new AccessDeniedException("Msg")).getErrorType());
    }

    @Test
    void testHandleAllNotFoundExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<NotFoundErrorResponse> actualHandleAllNotFoundExceptionsResult = globalExceptionHandler
                .handleAllNotFoundExceptions(new ResponseStatusException(HttpStatus.CONTINUE));
        assertNull(actualHandleAllNotFoundExceptionsResult.getBody());
        assertEquals("<404 NOT_FOUND Not Found,[]>", actualHandleAllNotFoundExceptionsResult.toString());
        assertEquals(HttpStatus.NOT_FOUND, actualHandleAllNotFoundExceptionsResult.getStatusCode());
        assertTrue(actualHandleAllNotFoundExceptionsResult.getHeaders().isEmpty());
    }

    @Test
    void testHandleAllBadRequestExceptions() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<BadRequestErrorResponse> actualHandleAllBadRequestExceptionsResult = globalExceptionHandler
                .handleAllBadRequestExceptions(new Exception("An error occurred"));
        assertNull(actualHandleAllBadRequestExceptionsResult.getBody());
        assertEquals("<400 BAD_REQUEST Bad Request,[]>", actualHandleAllBadRequestExceptionsResult.toString());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleAllBadRequestExceptionsResult.getStatusCode());
        assertTrue(actualHandleAllBadRequestExceptionsResult.getHeaders().isEmpty());
    }

    @Test
    void testHandleFileSizeExceedException() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<FileTooLargeErrorResponse> actualHandleFileSizeExceedExceptionResult = globalExceptionHandler
                .handleFileSizeExceedException(new Exception("An error occurred"));
        assertTrue(actualHandleFileSizeExceedExceptionResult.getHeaders().isEmpty());
        assertTrue(actualHandleFileSizeExceedExceptionResult.hasBody());
        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, actualHandleFileSizeExceedExceptionResult.getStatusCode());
        assertEquals("An error occurred", actualHandleFileSizeExceedExceptionResult.getBody().getMessage());
    }

    @Test
    void testHandleAllExceptionsWithStackTrace() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        ResponseEntity<InternalServerErrorResponse> actualHandleAllExceptionsWithStackTraceResult = globalExceptionHandler
                .handleAllExceptionsWithStackTrace(new Exception("An error occurred"));
        assertTrue(actualHandleAllExceptionsWithStackTraceResult.getHeaders().isEmpty());
        assertTrue(actualHandleAllExceptionsWithStackTraceResult.hasBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualHandleAllExceptionsWithStackTraceResult.getStatusCode());
        assertEquals("An error occurred", actualHandleAllExceptionsWithStackTraceResult.getBody().getMessage());
    }
}

