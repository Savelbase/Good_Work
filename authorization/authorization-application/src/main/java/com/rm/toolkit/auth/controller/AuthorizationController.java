package com.rm.toolkit.auth.controller;

import com.rm.toolkit.auth.dto.request.LogoutRequest;
import com.rm.toolkit.auth.dto.request.TokenRefreshRequest;
import com.rm.toolkit.auth.dto.request.UserDto;
import com.rm.toolkit.auth.dto.response.LoginErrorResponse;
import com.rm.toolkit.auth.dto.response.SuccessResponse;
import com.rm.toolkit.auth.dto.response.TokensResponse;
import com.rm.toolkit.auth.dto.response.error.*;
import com.rm.toolkit.auth.service.AuthenticationService;
import com.rm.toolkit.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Авторизация")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {

    private final AuthenticationService authService;
    private final TokenService tokenService;

    @Value("${authentication.token.accessTokenExpirationSec}")
    private Integer accessTokenExpirationSec;
    @Value("${authentication.token.type}")
    private String tokenType;

    @Operation(summary = "Получение access и refresh токенов при правильном логине и пароле")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получайте эти ваши токены", content = @Content(
                    schema = @Schema(implementation = TokensResponse.class))),
            // Todo договориться с фронтом о 401
//            @ApiResponse(responseCode = "401", description = "Неправильный пароль", content = @Content(
            @ApiResponse(responseCode = "400", description = "Неправильный пароль", content = @Content(
                    schema = @Schema(implementation = LoginErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким email не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_EMAIL\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "423", description = "Этот аккаунт заблокирован", content = @Content(
                    schema = @Schema(implementation = LockedErrorResponse.class)))
    })
    @PostMapping("/login")
    public TokensResponse login(@Valid @RequestBody UserDto userDto) {
        log.info("Вызван POST /login");

        Pair<String, String> tokens = authService.login(userDto.getEmail(), userDto.getPassword());
        return new TokensResponse(tokens.getLeft(), tokenType, accessTokenExpirationSec, tokens.getRight());
    }

    @Operation(summary = "Получить новые access и refresh токены",
            description = "Отправляете тот refresh токен, что у вас есть," +
                    " а в замен получаете новые access и refresh токены, а старый refresh токен работать перестанет.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получайте эти ваши токены", content = @Content(
                    schema = @Schema(implementation = TokensResponse.class))),
            @ApiResponse(responseCode = "401", description = "Просроченный токен", content = @Content(
                    schema = @Schema(implementation = UnauthorizedErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия", content = @Content(
                    schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден с таким id внутри токена (возникает, если пользователь удалён)", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "409", description = "Этот refresh-токен уже инвалидирован", content = @Content(
                    schema = @Schema(implementation = ConflictErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"REFRESH_TOKEN_WITH_NO_SESSIONS\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "422", description = "Передан не refresh токен", content = @Content(
                    schema = @Schema(implementation = UnprocessableEntityErrorResponse.class))),
            @ApiResponse(responseCode = "423", description = "Этот аккаунт заблокирован", content = @Content(
                    schema = @Schema(implementation = LockedErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public TokensResponse refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        log.info("Вызван POST /refresh");

        String oldRefreshToken = tokenRefreshRequest.getRefreshToken();
        Pair<String, String> tokens = tokenService.generateAccessAndRefreshTokens(oldRefreshToken);

        // Старый больше не нужен
        tokenService.deleteRefreshToken(oldRefreshToken);

        return new TokensResponse(tokens.getLeft(), tokenType, accessTokenExpirationSec, tokens.getRight());
    }

    @Operation(summary = "Инвалидация refresh токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токены успешно инвалидированы", content = @Content(
                    schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Просроченный токен", content = @Content(
                    schema = @Schema(implementation = UnauthorizedErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден с таким id внутри токена (возникает, если пользователь удалён)",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "409", description = "Этот refresh-токен уже инвалидирован", content = @Content(
                    schema = @Schema(implementation = ConflictErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Передан не refresh токен", content = @Content(
                    schema = @Schema(implementation = UnprocessableEntityErrorResponse.class)))
    })
    @PostMapping("/logout")
    public SuccessResponse revokeRefreshToken(@Valid @RequestBody LogoutRequest logoutRequest) {
        log.info("Вызван POST /logout");

        String token = logoutRequest.getRefreshToken();
        tokenService.deleteRefreshToken(token);

        return SuccessResponse.getGeneric();
    }
}
