package com.rm.toolkit.auth.test;

import com.rm.toolkit.auth.AuthorizationApplication;
import com.rm.toolkit.auth.dto.request.LogoutRequest;
import com.rm.toolkit.auth.dto.request.TokenRefreshRequest;
import com.rm.toolkit.auth.dto.request.UserDto;
import com.rm.toolkit.auth.dto.response.LoginErrorResponse;
import com.rm.toolkit.auth.dto.response.SuccessResponse;
import com.rm.toolkit.auth.dto.response.TokensResponse;
import com.rm.toolkit.auth.dto.response.error.*;
import com.rm.toolkit.auth.event.user.UserBlockedEvent;
import com.rm.toolkit.auth.exception.notfound.UserNotFoundException;
import com.rm.toolkit.auth.message.EventConsumer;
import com.rm.toolkit.auth.model.RefreshToken;
import com.rm.toolkit.auth.model.Role;
import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.repository.RefreshTokenRepository;
import com.rm.toolkit.auth.repository.RoleRepository;
import com.rm.toolkit.auth.repository.UserRepository;
import com.rm.toolkit.auth.security.AuthorityType;
import com.rm.toolkit.auth.test.util.DbUtil;
import com.rm.toolkit.auth.test.util.TokenFactory;
import com.rm.toolkit.auth.util.EventUtil;
import com.rm.toolkit.auth.util.JwtUtil;
import com.rm.toolkit.auth.util.factory.UserFactory;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("integration-test")
@SpringBootTest(classes = {AuthorizationApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = "classpath:/db/testdata/afterMigrate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class AuthorizationControllerIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private EventConsumer eventConsumer;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EventUtil eventUtil;
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private TokenFactory tokenFactory;
    @Autowired
    private DbUtil dbUtil;

    @Value("${authentication.token.accessTokenExpirationSec}")
    private int accessTokenExpirationSec;
    @Value("${authentication.maxLoginAttempts}")
    private Integer loginAttemptsMax;

    private static final AuthorityType[] authAuthorities = new AuthorityType[]{AuthorityType.AUTHORIZATION};

    private static final String LOGIN_PATH = "/auth/login";
    private static final String REFRESH_PATH = "/auth/refresh";
    private static final String LOGOUT_PATH = "/auth/logout";

    private static final String TEST_USER_EMAIL = "test1@rmtm.work";
    private static final String TEST_USER_PASSWORD = "password";
    private static String testUserId;

    private static final String EMPLOYEE_ROLE_ID = "579fc993-6123-419a-ae3c-96b0b230f834";

    final HttpHeaders jsonHeaders;

    public AuthorizationControllerIntegrationTests() {
        jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        jsonHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    @Transactional
    public void populateDatabaseWithTestUser() {
        Role employeeRole = new Role(EMPLOYEE_ROLE_ID, 1, authAuthorities, 1, false);
        roleRepository.save(employeeRole);

        User testUser = userFactory.createUser(TEST_USER_EMAIL, TEST_USER_PASSWORD, EMPLOYEE_ROLE_ID);
        userRepository.save(testUser);

        testUserId = testUser.getId();
    }

    @AfterEach
    @Transactional
    public void resetDatabaseState() {
        dbUtil.truncateAllTables();
    }

    @AfterAll
    public void dropAllTables() {
        dbUtil.dropAllTables();
    }

    @Test
    void login_ValidRequest_ValidResponse() {
        UserDto userDto = new UserDto(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, jsonHeaders);
        ResponseEntity<TokensResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGIN_PATH), httpEntity, TokensResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        TokensResponse tokensResponse = response.getBody();
        assertNotNull(tokensResponse);

        assertEquals("Bearer", tokensResponse.getTokenType());
        assertEquals(accessTokenExpirationSec, tokensResponse.getExpiresIn());

        String refreshToken = tokensResponse.getRefreshToken();
        validateRefreshToken(refreshToken);

        String accessToken = tokensResponse.getAccessToken();
        validateAccessToken(accessToken);
    }

    @Test
    void login_IncorrectPasswordManyTimes_BlockedAccount() {
        // GIVEN
        UserDto userDto = new UserDto(TEST_USER_EMAIL, "incorrect_password");
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, jsonHeaders);

        for (int i = (loginAttemptsMax - 1); i > 0; i--) {
            ResponseEntity<LoginErrorResponse> response = restTemplate.postForEntity(
                    createURLWithPort(LOGIN_PATH), httpEntity, LoginErrorResponse.class);

            // Todo договориться с фронтом о 401
//            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            LoginErrorResponse errorResponse = response.getBody();
            assertNotNull(errorResponse);

            assertEquals(UnauthorizedErrorResponse.ErrorType.INCORRECT_PASSWORD, errorResponse.getErrorType());
            assertEquals(i, errorResponse.getRemainingLoginAttempts());
        }

        User user = userRepository.findByEmail(TEST_USER_EMAIL).orElseThrow(
                () -> new UserNotFoundException(TEST_USER_EMAIL));
        // user-command должен был бы послать это событие
        UserBlockedEvent userBlockedEvent = new UserBlockedEvent();
        eventUtil.populateEventFields(userBlockedEvent, user.getId(), user.getVersion() + 1, user.getId(),
                new UserBlockedEvent.Payload());
        eventConsumer.handleUserEvent(userBlockedEvent);

        // WHEN
        ResponseEntity<LockedErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGIN_PATH), httpEntity, LockedErrorResponse.class);

        // THEN
        assertEquals(HttpStatus.LOCKED, response.getStatusCode());

        LockedErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(LockedErrorResponse.ErrorType.ACCOUNT_BLOCKED, errorResponse.getErrorType());
    }

    @Test
    void login_NonexistentUser_ErrorResponse() {
        String email = "login_nonexistent@rmtm.work";
        String password = "dkkvrljtv";
        UserDto userDto = new UserDto(email, password);

        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, jsonHeaders);
        ResponseEntity<NotFoundErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGIN_PATH), httpEntity, NotFoundErrorResponse.class, jsonHeaders);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        NotFoundErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(NotFoundErrorResponse.ErrorType.USER_NOT_FOUND_BY_EMAIL, errorResponse.getErrorType());
    }

    @Test
    void login_TwoIncorrectPasswordOneCorrect_CounterReset() {
        UserDto userDto = new UserDto(TEST_USER_EMAIL, "incorrect_password");
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, jsonHeaders);

        for (int i = (loginAttemptsMax - 1); i > 2; i--) {
            ResponseEntity<LoginErrorResponse> response = restTemplate.postForEntity(
                    createURLWithPort(LOGIN_PATH), httpEntity, LoginErrorResponse.class);

            // Todo договориться с фронтом о 401
//            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            LoginErrorResponse errorResponse = response.getBody();
            assertNotNull(errorResponse);

            assertEquals(i, errorResponse.getRemainingLoginAttempts());
        }

        UserDto userDto2 = new UserDto(TEST_USER_EMAIL, "password");
        HttpEntity<UserDto> httpEntity2 = new HttpEntity<>(userDto2, jsonHeaders);
        restTemplate.postForEntity(
                createURLWithPort(LOGIN_PATH), httpEntity2, SuccessResponse.class);

        ResponseEntity<LoginErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGIN_PATH), httpEntity, LoginErrorResponse.class);

        // Todo договориться с фронтом о 401
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        LoginErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);

        assertEquals(UnauthorizedErrorResponse.ErrorType.INCORRECT_PASSWORD, errorResponse.getErrorType());
        assertEquals(loginAttemptsMax - 1, errorResponse.getRemainingLoginAttempts());


    }


    @Test
    void refresh_ValidRequest_ValidResponse() {
        String refreshTokenString = populateDatabaseWithTestRefreshToken();
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshTokenString);

        HttpEntity<TokenRefreshRequest> httpEntity = new HttpEntity<>(tokenRefreshRequest, jsonHeaders);

        ResponseEntity<TokensResponse> response = restTemplate.postForEntity(
                createURLWithPort(REFRESH_PATH), httpEntity, TokensResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        TokensResponse tokensResponse = response.getBody();
        assertNotNull(tokensResponse);

        String refreshToken = tokensResponse.getRefreshToken();
        validateRefreshToken(refreshToken);

        String accessToken = tokensResponse.getAccessToken();
        validateAccessToken(accessToken);
    }

    @Test
    void refresh_ReusedToken_ErrorResponse() {
        String refreshTokenString = populateDatabaseWithTestRefreshToken();
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshTokenString);

        HttpEntity<TokenRefreshRequest> httpEntity = new HttpEntity<>(tokenRefreshRequest, jsonHeaders);

        restTemplate.postForEntity(createURLWithPort(REFRESH_PATH), httpEntity, TokensResponse.class);
        ResponseEntity<ConflictErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(REFRESH_PATH), httpEntity, ConflictErrorResponse.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ConflictErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(ConflictErrorResponse.ErrorType.REFRESH_TOKEN_WITH_NO_SESSIONS, errorResponse.getErrorType());
    }

    @Test
    void refresh_AccessToken_ErrorResponse() {
        String accessTokenString = tokenFactory.generateAccessToken(testUserId);
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(accessTokenString);

        HttpEntity<TokenRefreshRequest> httpEntity = new HttpEntity<>(tokenRefreshRequest, jsonHeaders);
        ResponseEntity<UnprocessableEntityErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(REFRESH_PATH), httpEntity, UnprocessableEntityErrorResponse.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

        UnprocessableEntityErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(UnprocessableEntityErrorResponse.ErrorType.NOT_REFRESH_TOKEN, errorResponse.getErrorType());
    }

    @Test
    void refresh_ExpiredToken_ErrorResponse() {
        Pair<String, Instant> expiredRefreshToken = tokenFactory.generateExpiredRefreshToken(testUserId);
        String expiredRefreshTokenString = expiredRefreshToken.getLeft();
        RefreshToken refreshToken = new RefreshToken(JwtUtil.getTokenHash(expiredRefreshTokenString),
                expiredRefreshToken.getRight(), testUserId);
        refreshTokenRepository.save(refreshToken);

        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(expiredRefreshTokenString);

        HttpEntity<TokenRefreshRequest> httpEntity = new HttpEntity<>(tokenRefreshRequest, jsonHeaders);
        ResponseEntity<UnauthorizedErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(REFRESH_PATH), httpEntity, UnauthorizedErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        UnauthorizedErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(UnauthorizedErrorResponse.ErrorType.EXPIRED_REFRESH_TOKEN, errorResponse.getErrorType());
    }

    @Test
    void refresh_BlockedUserToken_ErrorResponse() {
        // GIVEN
        Pair<String, Instant> refreshTokenWithDate = tokenFactory.generateRefreshToken(testUserId);
        refreshTokenRepository.save(new RefreshToken(JwtUtil.getTokenHash(refreshTokenWithDate.getLeft()),
                refreshTokenWithDate.getRight(), testUserId));

        UserDto userDto = new UserDto(TEST_USER_EMAIL, "incorrect_password");
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, jsonHeaders);

        for (int i = (loginAttemptsMax - 1); i > 0; i--) {
            restTemplate.postForEntity(
                    createURLWithPort(LOGIN_PATH), httpEntity, LoginErrorResponse.class);
        }

        User user = userRepository.findByEmail(TEST_USER_EMAIL).orElseThrow(
                () -> new UserNotFoundException(TEST_USER_EMAIL));
        // user-command должен был бы послать это событие
        UserBlockedEvent userBlockedEvent = new UserBlockedEvent();
        eventUtil.populateEventFields(userBlockedEvent, user.getId(), user.getVersion() + 1, user.getId(),
                new UserBlockedEvent.Payload());
        eventConsumer.handleUserEvent(userBlockedEvent);

        restTemplate.postForEntity(LOGIN_PATH, httpEntity, LockedErrorResponse.class);

        // WHEN
        TokenRefreshRequest request = new TokenRefreshRequest(refreshTokenWithDate.getLeft());
        HttpEntity<TokenRefreshRequest> httpEntity2 = new HttpEntity<>(request, jsonHeaders);
        ResponseEntity<LockedErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(REFRESH_PATH), httpEntity2, LockedErrorResponse.class);

        // THEN
        assertEquals(HttpStatus.LOCKED, response.getStatusCode());

        LockedErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(LockedErrorResponse.ErrorType.ACCOUNT_BLOCKED, errorResponse.getErrorType());
    }


    @Test
    void logout_ValidRequest_ValidResponse() {
        String refreshTokenString = populateDatabaseWithTestRefreshToken();
        LogoutRequest logoutRequest = new LogoutRequest(refreshTokenString);

        HttpEntity<LogoutRequest> httpEntity = new HttpEntity<>(logoutRequest, jsonHeaders);

        ResponseEntity<SuccessResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGOUT_PATH), httpEntity, SuccessResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        SuccessResponse successResponse = response.getBody();
        assertNotNull(successResponse);

        assertEquals("Операция успешно выполнена", successResponse.getMessage());
    }

    @Test
    void logout_ReusedToken_ErrorResponse() {
        String refreshTokenString = populateDatabaseWithTestRefreshToken();
        LogoutRequest logoutRequest = new LogoutRequest(refreshTokenString);

        HttpEntity<LogoutRequest> httpEntity = new HttpEntity<>(logoutRequest, jsonHeaders);

        restTemplate.postForEntity(createURLWithPort(LOGOUT_PATH), httpEntity, SuccessResponse.class);
        ResponseEntity<ConflictErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGOUT_PATH), httpEntity, ConflictErrorResponse.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ConflictErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(ConflictErrorResponse.ErrorType.REFRESH_TOKEN_WITH_NO_SESSIONS, errorResponse.getErrorType());
    }

    @Test
    void logout_AccessToken_ErrorResponse() {
        String accessTokenString = tokenFactory.generateAccessToken(testUserId);
        LogoutRequest logoutRequest = new LogoutRequest(accessTokenString);

        HttpEntity<LogoutRequest> httpEntity = new HttpEntity<>(logoutRequest, jsonHeaders);
        ResponseEntity<UnprocessableEntityErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGOUT_PATH), httpEntity, UnprocessableEntityErrorResponse.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

        UnprocessableEntityErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(UnprocessableEntityErrorResponse.ErrorType.NOT_REFRESH_TOKEN, errorResponse.getErrorType());
    }

    @Test
    void logout_ExpiredToken_ErrorResponse() {
        User user = userRepository.findByEmail(TEST_USER_EMAIL).orElseThrow(
                () -> new UserNotFoundException(TEST_USER_EMAIL));

        Pair<String, Instant> expiredRefreshToken = tokenFactory.generateExpiredRefreshToken(testUserId);
        String expiredRefreshTokenString = expiredRefreshToken.getLeft();
        RefreshToken refreshToken = new RefreshToken(JwtUtil.getTokenHash(expiredRefreshTokenString),
                expiredRefreshToken.getRight(), testUserId);
        refreshTokenRepository.save(refreshToken);

        LogoutRequest logoutRequest = new LogoutRequest(expiredRefreshTokenString);

        HttpEntity<LogoutRequest> httpEntity = new HttpEntity<>(logoutRequest, jsonHeaders);
        ResponseEntity<UnauthorizedErrorResponse> response = restTemplate.postForEntity(
                createURLWithPort(LOGOUT_PATH), httpEntity, UnauthorizedErrorResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        UnauthorizedErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(UnauthorizedErrorResponse.ErrorType.EXPIRED_REFRESH_TOKEN, errorResponse.getErrorType());
    }

    @Transactional
    public String populateDatabaseWithTestRefreshToken() {
        Pair<String, Instant> newToken = tokenFactory.generateRefreshToken(testUserId);
        String newTokenString = newToken.getLeft();
        String newTokenHash = JwtUtil.getTokenHash(newTokenString);

        User user = userRepository.findByEmail(TEST_USER_EMAIL).orElseThrow(
                () -> new UserNotFoundException(TEST_USER_EMAIL));

        RefreshToken refreshToken = new RefreshToken(newTokenHash, newToken.getRight(), testUserId);

        refreshTokenRepository.save(refreshToken);

        return newTokenString;
    }

    private void validateRefreshToken(String token) {
        Claims claims = jwtUtil.getClaimsFromJwtToken(token);

        assertFalse(claims.containsKey("authorities"));
        validateToken(claims);
    }

    private void validateAccessToken(String token) {
        Claims claims = jwtUtil.getClaimsFromJwtToken(token);

        assertTrue(claims.containsKey("authorities"));
        validateToken(claims);
    }

    private void validateToken(Claims claims) {
        assertNotNull(claims.getIssuer());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());

        assertTrue(claims.getIssuedAt().compareTo(new Date()) < 0);
        assertTrue(claims.getExpiration().compareTo(new Date()) > 0);

        assertNotNull(claims.getSubject());
        assertEquals(testUserId, claims.getSubject());
    }

    private String createURLWithPort(String uri) {
        return String.format("http://127.0.0.1:%s%s", port, uri);
    }
}


