package com.rm.toolkit.oneToOne.command.advice;

import com.rm.toolkit.oneToOne.command.dto.response.error.*;
import com.rm.toolkit.oneToOne.command.exception.*;
import com.rm.toolkit.oneToOne.command.exception.badrequest.FieldTooLongException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ExpiredTokenException.class})
    @Order(100)
    @ApiResponse(responseCode = "401", description = "Просроченный токен")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    UnauthorizedErrorResponse handleAllUnauthorizedExceptions(ResponseStatusException ex) {
        return new UnauthorizedErrorResponse(new Date(), UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN);
    }

    @ExceptionHandler(UserNotYourDisposalException.class)
    @Order(100)
    @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody
    ForbiddenErrorResponse handleAllForbiddenExceptions(UserNotYourDisposalException ex) {
        return new ForbiddenErrorResponse(new Date(), ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS);
    }

    @ExceptionHandler(UserInPoolIsAbsentException.class)
    @Order(100)
    @ApiResponse(responseCode = "412", description ="Пользователь с таким id отсутствует в пуле подчиненных")
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public @ResponseBody
    NotFoundErrorResponse handleUserIsNotSubordinateExceptions(UserInPoolIsAbsentException ex) {
        return new NotFoundErrorResponse(new Date(), NotFoundErrorResponse.ErrorType.USER_IN_POOL_IS_ABSENT);
    }

    @ExceptionHandler({OneToOneNotFoundException.class, UserNotFoundException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<NotFoundErrorResponse> handleAllNotFoundExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse.ErrorType errorType = null;
        if (ex instanceof OneToOneNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.ONE_TO_ONE_NOT_FOUND_BY_ID;
        } else if (ex instanceof UserNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.USER_NOT_FOUND;
        }
        NotFoundErrorResponse errorResponse = new NotFoundErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FieldTooLongException.class)
    public @ResponseBody
    @Order(10)
    ResponseEntity<BadRequestErrorResponse> handleBadRequestExceptions(ResponseStatusException ex) {
        BadRequestErrorResponse errorResponse = new BadRequestErrorResponse(new Date(), ex.getReason());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InappropriateTimeException.class)
    public @ResponseBody
    @Order(10)
    ResponseEntity<ConflictErrorResponse> handleAllConflictExceptions(ResponseStatusException ex) {
        return new ResponseEntity<>(
                new ConflictErrorResponse(new Date(), ConflictErrorResponse.ErrorType.ONE_TO_ONE_WRONG_TIME),
                HttpStatus.CONFLICT
        );
    }

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
