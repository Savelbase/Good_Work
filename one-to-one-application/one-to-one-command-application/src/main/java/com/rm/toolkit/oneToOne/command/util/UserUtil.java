package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.exception.UserInPoolIsAbsentException;
import com.rm.toolkit.oneToOne.command.exception.UserNotFoundException;
import com.rm.toolkit.oneToOne.command.model.User;
import com.rm.toolkit.oneToOne.command.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    /**
     * Осуществляет поиск пользователя с id = userId, если находит,
     * возвращает объект {@link com.rm.toolkit.oneToOne.command.model.User}, если не находит
     * - бросает исключение {@link com.rm.toolkit.oneToOne.command.exception.UserNotFoundException}.
     *
     * @param userId id пользователя
     *
     * @throws com.rm.toolkit.oneToOne.command.exception.UserNotFoundException пользователь с id = userId
     * не найден
     */
    public User findUserIfExists(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден", userId);
                    throw new UserNotFoundException(userId);
                });
    }


    /**
     * Осуществляет проверку, находится ли пользователь {@link com.rm.toolkit.oneToOne.command.model.User} с id = userId в пуле подчиненных пользователя с id = resourceManagerId,
     * если отсутствует, бросает исключение {@link com.rm.toolkit.oneToOne.command.exception.UserInPoolIsAbsentException},
     * в противном случае ничего не происходит.
     *
     * @param userId id пользователя, с которым назначается 1-2-1
     * @param resourceManagerId id пользователя, который назначает 1-2-1
     * @throws com.rm.toolkit.oneToOne.command.exception.UserInPoolIsAbsentException пользователь с id = userId
     * не найден в пуле подчиненных пользователя с id = resourceManagerId
     *
     * @author e.gordeev
     */
    public void checkIfUserExistInPool(String userId, String resourceManagerId) {
        User user = findUserIfExists(userId);
        User rm;
        if (Objects.isNull(user.getResourceManagerId())) {
            log.info("Пользователь с id={} отсутствует в пуле подчиненных пользователя с id= {}", userId, resourceManagerId);
            throw new UserInPoolIsAbsentException(userId, resourceManagerId);
        } else {
            rm = findUserIfExists(user.getResourceManagerId());
            if (!Objects.equals(rm.getId(), resourceManagerId)) {
               checkIfUserExistInPool(rm.getId(), resourceManagerId);
            }
        }
    }

}
