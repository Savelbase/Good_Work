package com.rm.toolkit.user.query.advice;

import com.rm.toolkit.user.query.dto.response.error.*;
import com.rm.toolkit.user.query.exception.notfound.CountryNotFoundException;
import com.rm.toolkit.user.query.exception.notfound.DepartmentNotFoundException;
import com.rm.toolkit.user.query.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.user.query.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.query.exception.token.ExpiredTokenException;
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


    @ExceptionHandler(AccessDeniedException.class)
    @Order(100)
    public @ResponseBody
    ResponseEntity<ForbiddenErrorResponse> handleAllForbiddenExceptions(AccessDeniedException ex) {
        ForbiddenErrorResponse errorResponse = new ForbiddenErrorResponse(new Date(), ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ExpiredTokenException.class})
    @Order(100)
    @ApiResponse(responseCode = "401", description = "Просроченный токен")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    UnauthorizedErrorResponse handleAllUnauthorizedExceptions(ResponseStatusException ex) {
        return new UnauthorizedErrorResponse(new Date(), UnauthorizedErrorResponse.ErrorType.EXPIRED_ACCESS_TOKEN);
    }

    @ExceptionHandler({CountryNotFoundException.class, DepartmentNotFoundException.class, RoleNotFoundException.class,
            UserNotFoundException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<NotFoundErrorResponse> handleAllNotFoundExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse.ErrorType errorType = null;
        if (ex instanceof CountryNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.COUNTRY_NOT_FOUND_BY_ID;
        } else if (ex instanceof DepartmentNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.DEPARTMENT_NOT_FOUND_BY_ID;
        } else if (ex instanceof RoleNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.ROLE_NOT_FOUND_BY_ID;
        } else if (ex instanceof UserNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.USER_NOT_FOUND_BY_ID;
        }
        NotFoundErrorResponse errorResponse = new NotFoundErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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
        errors = errors.substring(2);

        BadRequestErrorResponse badRequestErrorResponse = BadRequestErrorResponse.builder()
                .timestamp(new Date())
                .message(errors)
                .build();

        return new ResponseEntity<>(badRequestErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
