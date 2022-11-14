package com.rm.toolkit.comment.command.advise;

import com.rm.toolkit.comment.command.dto.response.error.*;
import com.rm.toolkit.comment.command.exception.badrequest.BadRequestException;
import com.rm.toolkit.comment.command.exception.badrequest.FieldTooLongException;
import com.rm.toolkit.comment.command.exception.notfound.CommentNotFoundException;
import com.rm.toolkit.comment.command.exception.token.ExpiredTokenException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Integer NUMBER_OF_LETTERS_LEFT_TO_DISPLAY_THE_MESSAGE = 2 ;



    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResponseEntity<InternalServerErrorResponse> handleAllExceptionsWithStackTrace(Exception ex) {
       log.error("Internal Server Error", ex);

        InternalServerErrorResponse internalServerErrorResponse = InternalServerErrorResponse.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(internalServerErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @Order(100)
    @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody
    ForbiddenErrorResponse handleAllForbiddenExceptions(AccessDeniedException ex) {
        return new ForbiddenErrorResponse(new Date(), ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        BadRequestErrorResponse badRequestErrorResponse = BadRequestErrorResponse.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(badRequestErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce("", (s, s2) -> String.format("%s, %s", s, s2));
        errors = errors.substring(NUMBER_OF_LETTERS_LEFT_TO_DISPLAY_THE_MESSAGE);

        BadRequestErrorResponse badRequestErrorResponse = BadRequestErrorResponse.builder()
                .timestamp(new Date())
                .message(errors)
                .build();

        return new ResponseEntity<>(badRequestErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CommentNotFoundException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<NotFoundErrorResponse> handleAllNotFoundExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse.ErrorType errorType = null;
        if (ex instanceof CommentNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.COMMENT_NOT_FOUND_BY_ID;
        }
        NotFoundErrorResponse errorResponse = new NotFoundErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, FieldTooLongException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<BadRequestErrorResponse> handleBadRequestExceptions(ResponseStatusException ex) {
        BadRequestErrorResponse errorResponse = new BadRequestErrorResponse(new Date(), ex.getReason());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ExpiredTokenException.class})
    @Order(100)
    @ApiResponse(responseCode = "401", description = "Просроченный токен")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    UnauthorizedErrorResponse handleAllUnauthorizedExceptions(ResponseStatusException ex) {
        return new UnauthorizedErrorResponse(new Date(), UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN);
    }

}
