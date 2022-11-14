package com.rm.toolkit.user.query.message;

import com.rm.toolkit.user.query.event.Event;
import com.rm.toolkit.user.query.event.EventPayload;
import com.rm.toolkit.user.query.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.query.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.query.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.query.event.role.RoleEditedEvent;
import com.rm.toolkit.user.query.event.user.*;
import com.rm.toolkit.user.query.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.user.query.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.query.message.projector.DepartmentProjector;
import com.rm.toolkit.user.query.message.projector.RoleProjector;
import com.rm.toolkit.user.query.message.projector.UserProjector;
import com.rm.toolkit.user.query.model.Department;
import com.rm.toolkit.user.query.model.Role;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.repository.DepartmentRepository;
import com.rm.toolkit.user.query.repository.RoleRepository;
import com.rm.toolkit.user.query.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final UserProjector userProjector;
    private final RoleProjector roleProjector;
    private final DepartmentProjector departmentProjector;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;

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
    public void handle(UserDepartmentChangedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил отдел после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserRmChangedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил RM после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(UserRoleChangedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил RM после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(ChangeUsersDepartmentEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил департамент пользователей после получения события", user.getId());
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(DepartmentCreatedEvent event) {
        Department department = departmentProjector.project(event);
        departmentRepository.save(department);

        log.info("Отдел с id {} создан после получения события", department.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(DepartmentEditedEvent event) {
        Department department = getDepartmentFromEvent(event);
        departmentProjector.project(event, department);
        departmentRepository.save(department);

        log.info("Отдел с id {} отредактирован после получения события", department.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(DepartmentDeletedEvent event) {
        Department department = getDepartmentFromEvent(event);
        departmentProjector.project(event, department);
        departmentRepository.save(department);

        log.info("Отдел с id {} удалён после получения события", department.getId());
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

    private Department getDepartmentFromEvent(Event<? extends EventPayload> event) {
        String departmentId = event.getEntityId();
        return departmentRepository.findById(departmentId).orElseThrow(() -> {
            log.info("Отдела с id {} не существует", departmentId);
            throw new RoleNotFoundException(departmentId);
        });
    }
}
