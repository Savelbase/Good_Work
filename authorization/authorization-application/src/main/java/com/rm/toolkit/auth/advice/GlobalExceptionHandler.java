package com.rm.toolkit.auth.advice;

import com.rm.toolkit.auth.dto.response.LoginErrorResponse;
import com.rm.toolkit.auth.dto.response.error.*;
import com.rm.toolkit.auth.exception.conflict.EmailAlreadyExistInWhitelistException;
import com.rm.toolkit.auth.exception.conflict.RefreshTokenWithNoSessionException;
import com.rm.toolkit.auth.exception.locked.AccountBlockedException;
import com.rm.toolkit.auth.exception.notfound.UserNotFoundException;
import com.rm.toolkit.auth.exception.unauthorized.ExpiredRefreshTokenException;
import com.rm.toolkit.auth.exception.unauthorized.PasswordIncorrectException;
import com.rm.toolkit.auth.exception.unprocessableentity.NotRefreshTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${authentication.maxLoginAttempts}")
    private Integer loginAttemptsMax;

    @ExceptionHandler(AccessDeniedException.class)
    @Order(100)
    public @ResponseBody
    ResponseEntity<ForbiddenErrorResponse> handleAllForbiddenExceptions(AccessDeniedException ex) {
        ForbiddenErrorResponse errorResponse = new ForbiddenErrorResponse(new Date(), ForbiddenErrorResponse.ErrorType.NOT_ENOUGH_RIGHTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ExpiredRefreshTokenException.class})
    @Order(100)
    public @ResponseBody
    ResponseEntity<UnauthorizedErrorResponse> handleAllUnauthorizedExceptions(ResponseStatusException ex) {
        UnauthorizedErrorResponse.ErrorType errorType = null;
        if (ex instanceof ExpiredRefreshTokenException) {
            errorType = UnauthorizedErrorResponse.ErrorType.EXPIRED_REFRESH_TOKEN;
        }
        UnauthorizedErrorResponse errorResponse = new UnauthorizedErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Todo Договориться с фронтом о 401
    @ExceptionHandler(PasswordIncorrectException.class)
    @Order(10)
    public @ResponseBody
    ResponseEntity<LoginErrorResponse> handlePasswordEnteredIncorrectlyExceptionx(PasswordIncorrectException ex) {
        LoginErrorResponse errorResponse = LoginErrorResponse.builder()
                .timestamp(new Date())
                .errorType(UnauthorizedErrorResponse.ErrorType.INCORRECT_PASSWORD)
                .remainingLoginAttempts(loginAttemptsMax - ex.getLoginAttempts())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmailAlreadyExistInWhitelistException.class, RefreshTokenWithNoSessionException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<ConflictErrorResponse> handleAllConflictExceptions(ResponseStatusException ex) {
        ConflictErrorResponse.ErrorType errorType = null;
        if (ex instanceof EmailAlreadyExistInWhitelistException) {
            errorType = ConflictErrorResponse.ErrorType.EMAIL_ALREADY_EXISTS_IN_WHITELIST;
        } else if (ex instanceof RefreshTokenWithNoSessionException) {
            errorType = ConflictErrorResponse.ErrorType.REFRESH_TOKEN_WITH_NO_SESSIONS;
        }
        ConflictErrorResponse errorResponse = new ConflictErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountBlockedException.class)
    public @ResponseBody
    @Order(10)
    ResponseEntity<LockedErrorResponse> handleAllLockedExceptions(ResponseStatusException ex) {
        LockedErrorResponse.ErrorType errorType = null;
        if (ex instanceof AccountBlockedException) {
            errorType = LockedErrorResponse.ErrorType.ACCOUNT_BLOCKED;
        }
        LockedErrorResponse errorResponse = new LockedErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.LOCKED);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<NotFoundErrorResponse> handleAllNotFoundExceptions(ResponseStatusException ex) {
        NotFoundErrorResponse.ErrorType errorType = null;
        if (ex instanceof UserNotFoundException) {
            if (Objects.requireNonNull(ex.getMessage()).contains("email")) {
                errorType = NotFoundErrorResponse.ErrorType.USER_NOT_FOUND_BY_EMAIL;
            } else {
                errorType = NotFoundErrorResponse.ErrorType.USER_NOT_FOUND_BY_ID;
            }
        }
        NotFoundErrorResponse errorResponse = new NotFoundErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotRefreshTokenException.class})
    public @ResponseBody
    @Order(10)
    ResponseEntity<UnprocessableEntityErrorResponse> handleAllUnprocessableEntityExceptions(ResponseStatusException ex) {
        UnprocessableEntityErrorResponse.ErrorType errorType = null;
        if (ex instanceof NotRefreshTokenException) {
            errorType = UnprocessableEntityErrorResponse.ErrorType.NOT_REFRESH_TOKEN;
        }
        UnprocessableEntityErrorResponse errorResponse = new UnprocessableEntityErrorResponse(new Date(), errorType);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
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
