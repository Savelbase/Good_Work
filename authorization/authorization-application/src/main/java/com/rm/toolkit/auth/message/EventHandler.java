package com.rm.toolkit.auth.message;

import com.rm.toolkit.auth.event.Event;
import com.rm.toolkit.auth.event.EventPayload;
import com.rm.toolkit.auth.event.role.RoleCreatedEvent;
import com.rm.toolkit.auth.event.role.RoleDeletedEvent;
import com.rm.toolkit.auth.event.role.RoleEditedEvent;
import com.rm.toolkit.auth.event.user.UserBlockedEvent;
import com.rm.toolkit.auth.event.user.UserCreatedEvent;
import com.rm.toolkit.auth.event.user.UserEditedEvent;
import com.rm.toolkit.auth.event.user.UserStatusChangedEvent;
import com.rm.toolkit.auth.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.auth.exception.notfound.UserNotFoundException;
import com.rm.toolkit.auth.service.MailService;
import com.rm.toolkit.auth.message.projector.RoleProjector;
import com.rm.toolkit.auth.message.projector.UserProjector;
import com.rm.toolkit.auth.model.Role;
import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.repository.RoleRepository;
import com.rm.toolkit.auth.repository.UserRepository;
import com.rm.toolkit.auth.repository.WhitelistRepository;
import com.rm.toolkit.auth.util.PassGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final UserProjector userProjector;
    private final RoleProjector roleProjector;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final WhitelistRepository whiteListRepository;
    private MailService mailService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserBlockedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} заблокирован после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserCreatedEvent event) {
        User user = userProjector.project(event);

        if (mailService != null) {
            if (whiteListRepository.existsByEmail(user.getEmail())) {
                String password = PassGenerator.generateRandom();
                mailService.sendPassword(password, user.getEmail());
                user.setPassword(passwordEncoder.encode(password));
            } else {
                String password = PassGenerator.generateEasyPassword();
                user.setPassword(passwordEncoder.encode(password));
            }
        } else {
            String password = PassGenerator.generateEasyPassword();
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);

        log.info("Пользователь с id {} создан после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserEditedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} отредактирован после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserStatusChangedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил статус после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(RoleCreatedEvent event) {
        Role role = roleProjector.project(event);
        roleRepository.save(role);

        log.info("Роль с id {} создана после получения события", role.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(RoleEditedEvent event) {
        Role role = getRoleFromEvent(event);
        roleProjector.project(event, role);
        roleRepository.save(role);

        log.info("Роль с id {} отредактирована после получения события", role.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(RoleDeletedEvent event) {
        Role role = getRoleFromEvent(event);
        roleProjector.project(event, role);
        roleRepository.save(role);

        log.info("Роль с id {} удалена после получения события", role.getId());
    }

    private User getUserFromEvent(Event<? extends EventPayload> event) {
        String userId = event.getEntityId();
        return userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователя с id {} не существует", userId);
            throw new UserNotFoundException(userId);
        });
    }

    private Role getRoleFromEvent(Event<? extends EventPayload> event) {
        String roleId = event.getEntityId();
        return roleRepository.findById(roleId).orElseThrow(() -> {
            log.info("Роли с id {} не существует", roleId);
            throw new RoleNotFoundException(roleId);
        });
    }

    @Autowired(required = false)
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
