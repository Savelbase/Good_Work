package com.rm.toolkit.oneToOne.query.util;

import com.rm.toolkit.oneToOne.query.exception.NoAuthorityException;
import com.rm.toolkit.oneToOne.query.exception.UserNotFoundException;
import com.rm.toolkit.oneToOne.query.model.User;
import com.rm.toolkit.oneToOne.query.repository.UserRepository;
import com.rm.toolkit.oneToOne.query.security.AuthorityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidationUtil {
    private final UserRepository userRepository;

    /**
     * @param userId id пользователя
     * @throws com.rm.toolkit.oneToOne.query.exception.UserNotFoundException пользователь с таким id не найден
     */
    public User findUserIfExist(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователь с id {} не найден", userId);
            throw new UserNotFoundException(userId);
        });
    }

    /**
     * Проверка прав пользователя для просмотра встреч у других сотрудников
     *
     * @throws com.rm.toolkit.oneToOne.query.exception.NoAuthorityException нет прав для просмотра one-to-one встреч у других пользователей
     */
    public void checkAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(AuthorityType.VIEW_ONE_TO_ONE.name()))) {
            log.error("Нет прав для просмотра one-to-one встреч у других пользователей");
            throw new NoAuthorityException("Нет прав для просмотра one-to-one встреч у других пользователей");
        }
    }
}
