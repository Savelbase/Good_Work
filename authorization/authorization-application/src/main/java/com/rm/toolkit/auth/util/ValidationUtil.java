package com.rm.toolkit.auth.util;

import com.rm.toolkit.auth.exception.AccountDeletedException;
import com.rm.toolkit.auth.exception.conflict.RefreshTokenWithNoSessionException;
import com.rm.toolkit.auth.exception.locked.AccountBlockedException;
import com.rm.toolkit.auth.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.auth.exception.notfound.UserNotFoundException;
import com.rm.toolkit.auth.exception.unprocessableentity.NotRefreshTokenException;
import com.rm.toolkit.auth.model.RefreshToken;
import com.rm.toolkit.auth.model.Role;
import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.model.type.StatusType;
import com.rm.toolkit.auth.repository.RefreshTokenRepository;
import com.rm.toolkit.auth.repository.RoleRepository;
import com.rm.toolkit.auth.repository.UserRepository;
import com.rm.toolkit.auth.security.AuthorityType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationUtil {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void validateUser(User user) {
        if (StatusType.BLOCKED.equals(user.getStatus())) {
            log.error("Учётная запись для пользователя " + user.getEmail() + " заблокирована");
            throw new AccountBlockedException();
        } else if (StatusType.DELETED.equals(user.getStatus())) {
            throw new AccountDeletedException();
        }

        Role role = findRoleIfExist(user.getRoleId());
        if (Arrays.stream(role.getAuthorities()).noneMatch(AuthorityType.AUTHORIZATION::equals)) {
            log.info("У пользователя с id {} не хватило прав для авторизации", user.getId());
            throw new AccessDeniedException(String.format("У пользователя с id %s не хватило прав для авторизации",
                    user.getId()));
        }
    }

    @Transactional(readOnly = true)
    public Claims validateRefreshTokenAndReturnClaims(String token) {
        Claims claims;
        try {
            claims = jwtUtil.getClaimsFromJwtToken(token);
        } catch (Exception e) {
            log.info("Передан не refresh токен");
            throw new NotRefreshTokenException();
        }

        if (!JwtUtil.isRefreshToken(claims)) {
            log.error("Передан не refresh токен");
            throw new NotRefreshTokenException();
        }
        if (!isRefreshTokenInWhitelist(token)) {
            log.error("С этим refresh токеном нет ассоциированных сессий");
            throw new RefreshTokenWithNoSessionException();
        }

        return claims;
    }

    @Transactional(readOnly = true)
    public boolean isRefreshTokenInWhitelist(String token) {
        String tokenHash = JwtUtil.getTokenHash(token);
        Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByHash(tokenHash);
        return foundRefreshToken.isPresent();
    }

    @Transactional(readOnly = true)
    public User getUserFromTokenClaims(Claims claims) throws UserNotFoundException {
        String id = claims.getSubject();

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User findUserIfExists(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователь с id {} не найден", userId);
            throw new UserNotFoundException(userId);
        });
    }

    public Role findRoleIfExist(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> {
            log.error("Роль с id {} не найден", roleId);
            throw new RoleNotFoundException(roleId);
        });
    }
}
