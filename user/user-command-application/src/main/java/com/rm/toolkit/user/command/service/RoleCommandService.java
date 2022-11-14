package com.rm.toolkit.user.command.service;

import com.rm.toolkit.user.command.dto.command.role.CreateRoleCommand;
import com.rm.toolkit.user.command.dto.command.role.EditRoleCommand;
import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.command.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.command.event.role.RoleEditedEvent;
import com.rm.toolkit.user.command.event.user.UserRmChangedEvent;
import com.rm.toolkit.user.command.event.user.UserRoleChangedEvent;
import com.rm.toolkit.user.command.exception.conflict.UserHasDepartmentsException;
import com.rm.toolkit.user.command.exception.unprocessableentity.*;
import com.rm.toolkit.user.command.message.EventPublisher;
import com.rm.toolkit.user.command.message.projector.RoleProjector;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.RoleRepository;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.util.EventUtil;
import com.rm.toolkit.user.command.util.SubordinateTreeUtil;
import com.rm.toolkit.user.command.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleCommandService {

    private final RoleProjector roleProjector;
    private final UserProjector userProjector;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final SubordinateTreeUtil subordinateTreeUtil;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;

    @Value("${role.employee}")
    private String employeeRoleId;
    @Value("${role.admin}")
    private String adminRoleId;

    public static final List<AuthorityType> HEAD_AUTHORITIES = List.of(AuthorityType.USER_STATUS_SETTINGS,
            AuthorityType.ADD_EMPLOYEE_TO_DEPARTMENT);

    /**
     * @param command  имя, приоритет и права новой роли
     * @param authorId id пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.conflict.RoleAlreadyExistsException                  роль с таким именем уже существует
     * @throws RolePriorityOutOfBoundsException                                                           приоритет должен находится между 1 и 9 включительно
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException приоритет должен находится между 1 и 9 включительно
     * @throws OnlyAdminShouldHaveSettingsAuthorityException                                              нельзя создать вторую администраторскую раль. Администраторская роль определяеся по наличию права SETTINGS.
     */
    @Transactional
    public String createRole(CreateRoleCommand command, String authorId) {
        validationUtil.checkIfRoleNameWithinLimit(command.getName());
        validationUtil.checkThatRoleDoesntExist(command.getName());
        validationUtil.checkIfRolePriorityWithinBounds(command.getPriority());
        validationUtil.validateAuthorities(command.getAuthorities());

        RoleCreatedEvent event = new RoleCreatedEvent();
        RoleCreatedEvent.Payload payload = RoleCreatedEvent.Payload.builder()
                .name(command.getName())
                .priority(command.getPriority())
                .authorities(command.getAuthorities())
                .build();
        eventUtil.populateEventFields(event, UUID.randomUUID().toString(), 1, authorId, payload, true);

        eventPublisher.publishNoReupload(event);

        Role role = roleProjector.project(event);
        roleRepository.save(role);
        return role.getId();
    }

    /**
     * @param roleId   id изменяемой роли
     * @param command  новые имя, приоритет и права изменяемой роли
     * @param authorId id пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException      роль с таким id не найдена
     * @throws com.rm.toolkit.user.command.exception.conflict.RoleAlreadyExistsException роль с таким именем уже существует
     * @throws RolePriorityOutOfBoundsException                                          приоритет должен находится между 1 и 9 включительно
     * @throws OnlyAdminShouldHaveSettingsAuthorityException                             нельзя создать вторую администраторскую раль
     * @throws AdminRoleIsNotEditableException                                           нельзя редактировать админскую роль
     * @throws UserHasDepartmentsException                                               чтобы удалить роль, нужно чтобы не было глав отделов с такой ролью
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void editRole(String roleId, EditRoleCommand command, String authorId) {
        validationUtil.checkIfRoleNameWithinLimit(command.getName());
        Role role = validationUtil.findRoleIfExist(roleId);
        User author = validationUtil.findUserIfExist(authorId);

        if (!command.getName().equals(role.getName())) {
            validationUtil.checkThatRoleDoesntExist(command.getName());
        }
        validationUtil.checkIfRolePriorityWithinBounds(command.getPriority());
        validationUtil.validateAuthorities(command.getAuthorities());

        if (validationUtil.isAdmin(role)) {
            log.info("Попытка отредактировать роль ADMIN");
            throw new AdminRoleIsNotEditableException(role.getName());
        }

        if (role.isImmutable()) {
            log.info("Попытка редактировать базовую роль {}", role.getName());
            throw new BaseRoleNotEditableException(role.getName());
        }

        if (validationUtil.isDepartmentHead(role) && !command.getAuthorities().containsAll(HEAD_AUTHORITIES)) {
            Set<User> heads = userRepository.findHeadsWithRoleId(roleId);
            if (!heads.isEmpty()) {
                log.info("Попытка убрать права USER_STATUS_SETTINGS или ADD_EMPLOYEE_TO_DEPARTMENT у главы отдела");
                throw new UserHasDepartmentsException(heads.stream().findFirst().orElseThrow().getId());
            }
        }

        boolean roleNameChanged = !role.getName().equals(command.getName());

        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String roleEditedEventId = UUID.randomUUID().toString();

        // Если приоритет роли уменьшился, то те прямые подчинённые пользователей с этой ролью, у которых роль >= приоритету новой роли должны пойти вверх по дереву подчинённых
        if (role.getPriority() > command.getPriority()) {
            Set<User> usersToMoveUp = userRepository.findAllByResourceManagerRoleIdAndRolePriorityBetween(roleId,
                    command.getPriority());
            for (User user : usersToMoveUp) {
                User rm = validationUtil.findUserIfExist(user.getResourceManagerId());
                rm = validationUtil.findUserIfExist(rm.getResourceManagerId());
                UserRmChangedEvent userRmChangedEvent = new UserRmChangedEvent();
                UserRmChangedEvent.Payload userRmChangedPayload = UserRmChangedEvent.Payload.builder()
                        .resourceManagerId(rm.getId())
                        .resourceManagerFirstName(rm.getFirstName())
                        .resourceManagerLastName(rm.getLastName())
                        .build();
                eventUtil.populateEventFields(userRmChangedEvent, user.getId(), user.getVersion() + 1, authorId,
                        userRmChangedPayload, roleEditedEventId);
                userProjector.project(userRmChangedEvent, user);
                eventQueue.add(userRmChangedEvent);
            }
        }

        RoleEditedEvent event = new RoleEditedEvent();
        RoleEditedEvent.Payload payload = RoleEditedEvent.Payload.builder()
                .name(command.getName())
                .priority(command.getPriority())
                .authorities(command.getAuthorities())
                .build();
        event.setId(roleEditedEventId);
        eventUtil.populateEventFields(event, role.getId(), role.getVersion() + 1, authorId, payload, false);
        roleProjector.project(event, role);
        roleRepository.save(role);
        eventQueue.add(event);

        if (roleNameChanged) {
            Set<User> usersWithThisRole = userRepository.findAllByRoleId(roleId);
            for (User user : usersWithThisRole) {
                addToQueueUserRoleChangedEvents(eventQueue, user, role, roleEditedEventId, author);
            }
        }

        eventPublisher.publish(eventQueue);
    }

    /**
     * @param roleId   id удаляемой роли
     * @param authorId id пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException роль с таким id не найдена
     * @throws UserHasDepartmentsException                                          чтобы удалить роль, нужно чтобы не было глав отделов с такой ролью
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteRole(String roleId, String authorId) {
        Role role = validationUtil.findRoleIfExist(roleId);
        User author = validationUtil.findUserIfExist(authorId);

        if (role.isImmutable()) {
            log.info("Попытка удалить базовую роль {}", role.getName());
            throw new BaseRoleNotDeletableException(role.getName());
        }

        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String roleDeletedEventId = UUID.randomUUID().toString();

        Set<User> usersWithThisRole = userRepository.findAllByRoleId(roleId);
        if (!usersWithThisRole.isEmpty()) {
            Role employeeRole = validationUtil.findRoleIfExist(employeeRoleId);

            for (User user : usersWithThisRole) {
                addToQueueUserRoleChangedEvents(eventQueue, user, employeeRole, roleDeletedEventId, author);
            }
        }

        RoleDeletedEvent event = new RoleDeletedEvent();
        RoleDeletedEvent.Payload payload = new RoleDeletedEvent.Payload();
        event.setId(roleDeletedEventId);
        eventUtil.populateEventFields(event, roleId, role.getVersion() + 1, authorId, payload, false);
        roleProjector.project(event, role);
        roleRepository.delete(role);
        eventQueue.add(event);

        eventPublisher.publish(eventQueue);
    }

    @Transactional
    public void addToQueueUserRoleChangedEvents(Queue<Event<? extends EventPayload>> eventQueue, User user, Role role,
                                                String parentEventId, User author) {
        UserRoleChangedEvent.Payload payload = UserRoleChangedEvent.Payload.builder()
                .roleId(role.getId())
                .roleName(role.getName())
                .isRm(validationUtil.isRm(role))
                .build();
        UserRoleChangedEvent userRoleChangedEvent = new UserRoleChangedEvent();
        eventUtil.populateEventFields(userRoleChangedEvent, user.getId(), user.getVersion() + 1, author.getId(),
                payload, true, parentEventId);
        userProjector.project(userRoleChangedEvent, user);

        eventQueue.add(userRoleChangedEvent);
    }
}
