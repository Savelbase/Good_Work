package com.rm.toolkit.mediaStorage.command.advice;

import com.rm.toolkit.mediaStorage.command.dto.response.error.*;
import com.rm.toolkit.mediaStorage.command.exception.ExpiredTokenException;
import com.rm.toolkit.mediaStorage.command.exception.FileNotFoundException;
import com.rm.toolkit.mediaStorage.command.exception.FileNotProvidedException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ExpiredTokenException.class})
    @Order(100)
    @ApiResponse(responseCode = "401", description = "Просроченный токен")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    UnauthorizedErrorResponse handleAllUnauthorizedExceptions(ResponseStatusException ex) {
        return new UnauthorizedErrorResponse(new Date(), UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @Order(100)
    @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody
    ForbiddenErrorResponse handleAllForbiddenExceptions(AccessDeniedException ex) {
        return new ForbiddenErrorResponse(new Date(), ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS);
    }

    @ExceptionHandler({FileNotFoundException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<NotFoundErrorResponse> handleAllNotFoundExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse errorResponse = null;
        if (ex instanceof FileNotFoundException) {
            errorResponse = new NotFoundErrorResponse(new Date(), NotFoundErrorResponse.ErrorType.FILE_NOT_FOUND);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FileNotProvidedException.class, MimeTypeException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<BadRequestErrorResponse> handleAllBadRequestExceptions(Exception ex) {
        BadRequestErrorResponse errorResponse = null;
        if (ex instanceof MimeTypeException) {
            errorResponse = new BadRequestErrorResponse(new Date(), BadRequestErrorResponse.ErrorType.WRONG_EXTENSION.toString());
        } else if (ex instanceof FileNotProvidedException) {
            errorResponse = new BadRequestErrorResponse(new Date(), BadRequestErrorResponse.ErrorType.FILE_NOT_PROVIDED.toString());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MultipartException.class)
    public @ResponseBody
    @Order(10)
    ResponseEntity<FileTooLargeErrorResponse> handleFileSizeExceedException(Exception ex) {
        return new ResponseEntity<>(new FileTooLargeErrorResponse(new Date(), ex.getMessage()),
                HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResponseEntity<InternalServerErrorResponse> handleAllExceptionsWithStackTrace(Exception ex) {
        ex.printStackTrace();

        InternalServerErrorResponse internalServerErrorResponse = InternalServerErrorResponse.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(internalServerErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected @NonNull
    ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce("", (s, s2) -> String.format("%s, %s", s, s2));
        // Delete first ", " generated by reduce
        errors = errors.substring(2);

        BadRequestErrorResponse badRequestErrorResponse = BadRequestErrorResponse.builder()
                .timestamp(new Date())
                .message(errors)
                .build();

        return new ResponseEntity<>(badRequestErrorResponse, HttpStatus.BAD_REQUEST);
    }
}