package com.rm.toolkit.auth.service;

import com.rm.toolkit.auth.event.auth.UserMaxLoginAttemptsReachedEvent;
import com.rm.toolkit.auth.exception.locked.AccountBlockedException;
import com.rm.toolkit.auth.exception.notfound.UserNotFoundException;
import com.rm.toolkit.auth.exception.unauthorized.PasswordIncorrectException;
import com.rm.toolkit.auth.message.EventPublisher;
import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.model.type.StatusType;
import com.rm.toolkit.auth.repository.UserRepository;
import com.rm.toolkit.auth.util.EventUtil;
import com.rm.toolkit.auth.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final TokenService tokenService;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;

    @Value("${authentication.maxLoginAttempts}")
    private Integer loginAttemptsMax;

    private static final Integer ADMIN_PRIORITY = 10;

    /**
     * @param email    email пользователя
     * @param password пароль пользователя
     * @return Pair<access-токен, refresh-токен>
     * @throws UserNotFoundException                                     пользователь с таким email не найден
     * @throws PasswordIncorrectException                                неправильный пароль
     * @throws AccountBlockedException                                   аккаунт заблокирован после loginAttemptsMax попыток входа с неправильным паролем
     * @throws org.springframework.security.access.AccessDeniedException у пользователя нет права AUTHORIZATION
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, noRollbackFor = {AccountBlockedException.class,
            PasswordIncorrectException.class})
    public Pair<String, String> login(String email, String password) {
        String lowerCaseEmail = email.toLowerCase();

        User user = userRepository.findByEmail(lowerCaseEmail).orElseThrow(() -> {
            log.info("Пользователя с email {} не существует", lowerCaseEmail);
            throw new UserNotFoundException(lowerCaseEmail, true);
        });

        validationUtil.validateUser(user);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Пароль для пользователя {} введён неправильно", user.getEmail());

            user.setLoginAttempts(user.getLoginAttempts() + 1);
            userRepository.save(user);

            checkIfShouldBeBlocked(user);

            if (StatusType.BLOCKED.equals(user.getStatus())) {
                throw new AccountBlockedException();
            } else {
                throw new PasswordIncorrectException(user.getLoginAttempts());
            }
        }

        Pair<String, String> tokens = tokenService.generateAccessAndRefreshTokens(user);

        resetLoginAttemptsCounter(user);

        return tokens;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void checkIfShouldBeBlocked(User user) {

        if (loginAttemptsMax.equals(user.getLoginAttempts())
                && !(validationUtil.findRoleIfExist(user.getRoleId())).getPriority().equals(ADMIN_PRIORITY)) {

            log.error("Пользователь {} заблокирован", user.getEmail());
            user.setLoginAttempts(0);

            UserMaxLoginAttemptsReachedEvent event = new UserMaxLoginAttemptsReachedEvent();
            eventUtil.populateEventFields(event, user.getId(), 1, user.getId(),
                    new UserMaxLoginAttemptsReachedEvent.Payload());

            eventPublisher.publishWithReupload(event);
            throw new AccountBlockedException();
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void resetLoginAttemptsCounter(User user) {
        user.setLoginAttempts(0);

        userRepository.save(user);
    }
}
