package com.rm.toolkit.user.query.util;

import com.rm.toolkit.user.query.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationUtil {

    private final UserRepository userRepository;

    public User findUserIfExists(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователь с id {} не найден", userId);
            throw new UserNotFoundException(userId);
        });
    }
}
