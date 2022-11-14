package com.rm.toolkit.feedbackcommandapplication.advice;

import com.rm.toolkit.feedbackcommandapplication.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.feedbackcommandapplication.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.feedbackcommandapplication.dto.response.error.InternalServerErrorResponse;
import com.rm.toolkit.feedbackcommandapplication.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.feedbackcommandapplication.exception.badrequest.BadRequestException;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.DepartmentNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.FeedbackNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.OneToOneNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.UserNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
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

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int SUBSTRING_STEP = 2;

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

    @ExceptionHandler({FeedbackNotFoundException.class, OneToOneNotFoundException.class,
            UserNotFoundException.class, DepartmentNotFoundException.class})
    @Order(10)
    @ApiResponse(responseCode = "404", description = "Фидбек не найден")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ResponseEntity<NotFoundErrorResponse> handleAllForbiddenExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse.ErrorType errorType = null;
        if (ex instanceof FeedbackNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.FEEDBACK_NOT_FOUND_BY_ID;
        } else if (ex instanceof OneToOneNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.ONE_TO_ONE_NOT_FOUND_BY_ID;
        } else if (ex instanceof UserNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.USER_NOT_FOUND_BY_ID;
        } else if (ex instanceof DepartmentNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.DEPARTMENT_NOT_FOUND_BY_ID;
        }

        NotFoundErrorResponse errorResponse = new NotFoundErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @Order(10)
    public @ResponseBody
    ResponseEntity<BadRequestErrorResponse> handleBadRequestExceptions(ResponseStatusException ex) {
        BadRequestErrorResponse errorResponse = BadRequestErrorResponse.builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
        // Delete first ", " generated by reduce
        errors = errors.substring(SUBSTRING_STEP);

        BadRequestErrorResponse badRequestErrorResponse = BadRequestErrorResponse.builder()
                .timestamp(new Date())
                .message(errors)
                .build();

        return new ResponseEntity<>(badRequestErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
