package com.rm.toolkit.auth.service;

import com.rm.toolkit.auth.exception.conflict.RefreshTokenWithNoSessionException;
import com.rm.toolkit.auth.exception.notfound.UserNotFoundException;
import com.rm.toolkit.auth.exception.unprocessableentity.NotRefreshTokenException;
import com.rm.toolkit.auth.model.RefreshToken;
import com.rm.toolkit.auth.model.Role;
import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.repository.RefreshTokenRepository;
import com.rm.toolkit.auth.security.AuthorityType;
import com.rm.toolkit.auth.util.JwtUtil;
import com.rm.toolkit.auth.util.ValidationUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ValidationUtil validationUtil;

    @Value("${authentication.token.refreshTokenExpirationSec}")
    private Long refreshTokenExpirationSec;
    @Value("${authentication.token.accessTokenExpirationSec}")
    private int accessTokenExpirationSec;
    @Value("${authentication.token.issuer}")
    private String jwtIssuer;


    /**
     * @param oldRefreshToken токен, указанный в refresh запросе
     * @return Pair<access0token, refresh-token>
     */
    @Transactional
    public Pair<String, String> generateAccessAndRefreshTokens(String oldRefreshToken) {
        Claims claims = validationUtil.validateRefreshTokenAndReturnClaims(oldRefreshToken);
        User user = validationUtil.findUserIfExists(claims.getSubject());

        return generateAccessAndRefreshTokens(user);
    }

    /**
     * @param user кому токен генерируем
     * @return Pair<access0token, refresh-token>
     */
    @Transactional
    public Pair<String, String> generateAccessAndRefreshTokens(User user) {
        validationUtil.validateUser(user);
        Role role = validationUtil.findRoleIfExist(user.getRoleId());
        if (Arrays.stream(role.getAuthorities()).noneMatch(AuthorityType.AUTHORIZATION::equals)) {
            log.info("У пользователя с id {} не хватило прав для аутентификации", user.getId());
            throw new AccessDeniedException(String.format("У пользователя с id %s не хватило прав для аутентификации",
                    user.getId()));
        }

        Instant currentTime = Instant.now();
        Instant accessTokenExpirationDate = currentTime.plus(accessTokenExpirationSec, ChronoUnit.SECONDS);
        Instant refreshTokenExpirationDate = currentTime.plus(refreshTokenExpirationSec, ChronoUnit.SECONDS);

        String accessToken = jwtUtil.generateAccessToken(user.getId(), jwtIssuer, new Date(currentTime.toEpochMilli()),
                new Date(accessTokenExpirationDate.toEpochMilli()), role.getAuthorities(), role.getPriority());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), jwtIssuer, new Date(currentTime.toEpochMilli()),
                new Date(refreshTokenExpirationDate.toEpochMilli()));

        String tokenHash = JwtUtil.getTokenHash(refreshToken);

        /* Интеграционный тест настолько быстрый, что новый токен создаётся в ту же секунду, что и старый, а раз
           время одинаковое, то и токены будут одинаковыми. Поэтому токен добавляется, только если его ещё нет. */
        if (refreshTokenRepository.findByHash(tokenHash).isEmpty()) {
            refreshTokenRepository.save(new RefreshToken(tokenHash, refreshTokenExpirationDate, user.getId()));
        }

        return Pair.of(accessToken, refreshToken);
    }

    /**
     * @param token какой токен удалить из whitelist
     * @throws NotRefreshTokenException           передан не refresh-токен
     * @throws UserNotFoundException              пользователя с таким id внутри токен нет. Такое может произойти, если пользователь удалён, а токен ещё жив)
     * @throws RefreshTokenWithNoSessionException токена нет в whitelist
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteRefreshToken(String token) {
        Claims claims = validationUtil.validateRefreshTokenAndReturnClaims(token);
        validationUtil.findUserIfExists(claims.getSubject());

        String hash = JwtUtil.getTokenHash(token);

        Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByHash(hash);

        if (foundRefreshToken.isEmpty()) {
            log.error("С этим refresh токеном нет ассоциированных сессий");
            throw new RefreshTokenWithNoSessionException();
        }

        refreshTokenRepository.deleteById(foundRefreshToken.get().getHash());
    }

    // Раз в час
    @Scheduled(fixedRate = 3600000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteExpiredTokens() {
        log.info("Просроченные токены удалены из бд");
        refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
    }
}
