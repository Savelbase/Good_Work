package com.rm.toolkit.feedbackcommandapplication.util;

import com.rm.toolkit.feedbackcommandapplication.exception.notfound.UserNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.model.User;
import com.rm.toolkit.feedbackcommandapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUtil {

    private final UserRepository userRepository;

    public User findUserIfExists(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден", userId);
                    throw new UserNotFoundException(userId);
                });
    }
}
