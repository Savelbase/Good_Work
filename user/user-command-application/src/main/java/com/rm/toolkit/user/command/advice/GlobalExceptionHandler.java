package com.rm.toolkit.user.command.advice;

import com.rm.toolkit.user.command.dto.response.error.*;
import com.rm.toolkit.user.command.exception.badrequest.BadRequestException;
import com.rm.toolkit.user.command.exception.badrequest.FieldTooLongException;
import com.rm.toolkit.user.command.exception.conflict.*;
import com.rm.toolkit.user.command.exception.notfound.*;
import com.rm.toolkit.user.command.exception.token.ExpiredTokenException;
import com.rm.toolkit.user.command.exception.unprocessableentity.*;
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

    @ExceptionHandler({ActivityNotFoundException.class, AuthorityNotFoundException.class, CityNotFoundException.class,
            CountryNotFoundException.class, DepartmentNotFoundException.class, RoleNotFoundException.class,
            UserNotFoundException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<NotFoundErrorResponse> handleAllNotFoundExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse.ErrorType errorType = null;
        if (ex instanceof ActivityNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.ACTIVITY_NOT_FOUND_BY_ID;
        } else if (ex instanceof AuthorityNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.AUTHORITY_NOT_FOUND_BY_ID;
        } else if (ex instanceof CityNotFoundException) {
            errorType = NotFoundErrorResponse.ErrorType.CITY_NOT_FOUND_BY_ID;
        } else if (ex instanceof CountryNotFoundException) {
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

    @ExceptionHandler({DepartmentAlreadyExistsException.class, RoleAlreadyExistsException.class,
            UserAlreadyExistException.class, UserHasDepartmentsException.class, UserCantBecomeDepartmentHeadException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<ConflictErrorResponse> handleAllConflictExceptions(ResponseStatusException ex) {
        ConflictErrorResponse.ErrorType errorType = null;
        if (ex instanceof DepartmentAlreadyExistsException) {
            errorType = ConflictErrorResponse.ErrorType.DEPARTMENT_ALREADY_EXISTS_BY_NAME;
        } else if (ex instanceof RoleAlreadyExistsException) {
            errorType = ConflictErrorResponse.ErrorType.ROLE_ALREADY_EXISTS_BY_NAME;
        } else if (ex instanceof UserAlreadyExistException) {
            errorType = ConflictErrorResponse.ErrorType.USER_ALREADY_EXISTS_BY_EMAIL;
        } else if (ex instanceof UserHasDepartmentsException) {
            errorType = ConflictErrorResponse.ErrorType.USER_HAS_DEPARTMENTS;
        } else if (ex instanceof UserCantBecomeDepartmentHeadException) {
            errorType = ConflictErrorResponse.ErrorType.USER_CANT_BECOME_DEPARTMENT_HEAD;
        }
        ConflictErrorResponse errorResponse = new ConflictErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({OnlyAdminShouldHaveSettingsAuthorityException.class, RolePriorityOutOfBoundsException.class,
            AdminRoleIsNotEditableException.class, BaseRoleNotDeletableException.class, BaseRoleNotEditableException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<UnprocessableEntityErrorResponse> handleAllUnprocessableEntityExceptions(ResponseStatusException ex) {
        UnprocessableEntityErrorResponse.ErrorType errorType = null;
        if (ex instanceof OnlyAdminShouldHaveSettingsAuthorityException) {
            errorType = UnprocessableEntityErrorResponse.ErrorType.ONLY_ADMIN_SHOULD_HAVE_SETTINGS_AUTHORITY;
        } else if (ex instanceof RolePriorityOutOfBoundsException) {
            errorType = UnprocessableEntityErrorResponse.ErrorType.ROLE_PRIORITY_OUT_OF_BOUNDS;
        } else if (ex instanceof AdminRoleIsNotEditableException) {
            errorType = UnprocessableEntityErrorResponse.ErrorType.ADMIN_ROLE_IS_NOT_EDITABLE;
        } else if (ex instanceof BaseRoleNotDeletableException) {
            errorType = UnprocessableEntityErrorResponse.ErrorType.BASE_ROLE_NOT_DELETABLE;
        } else if (ex instanceof BaseRoleNotEditableException) {
            errorType = UnprocessableEntityErrorResponse.ErrorType.BASE_ROLE_NOT_EDITABLE;
        }
        UnprocessableEntityErrorResponse errorResponse = new UnprocessableEntityErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({BadRequestException.class, FieldTooLongException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<BadRequestErrorResponse> handleBadRequestExceptions(ResponseStatusException ex) {
        BadRequestErrorResponse errorResponse = new BadRequestErrorResponse(new Date(), ex.getReason());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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