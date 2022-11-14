package com.rm.toolkit.user.command.service;

import com.rm.toolkit.user.command.dto.command.user.ChangeUsersDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.user.ChangeUserDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.user.CreateUserCommand;
import com.rm.toolkit.user.command.dto.command.user.EditUserCommand;
import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.user.*;
import com.rm.toolkit.user.command.exception.conflict.UserHasDepartmentsException;
import com.rm.toolkit.user.command.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.command.message.EventPublisher;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.*;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.util.EventUtil;
import com.rm.toolkit.user.command.util.SubordinateTreeUtil;
import com.rm.toolkit.user.command.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserProjector userProjector;
    private final ValidationUtil validationUtil;
    private final SubordinateTreeUtil subordinateTreeUtil;

    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;

    /**
     * @param command            команда на создание пользователя
     * @param authorId           id пользователя, который делает данную операцию
     * @param authorRolePriority приоритет роли пользователя, который делает данную операцию
     * @param authorAuthorities  список прав пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.UserNotFoundException                       RM с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.conflict.UserAlreadyExistException                   пользователь с таким email уже существует
     * @throws com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException                 отдел с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.notfound.ActivityNotFoundException                   активности не найдены
     * @throws com.rm.toolkit.user.command.exception.notfound.CityNotFoundException                       город с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException приоритет роли должен быть в диапазное 1<x<ваш приоритет
     * @throws AccessDeniedException                                                                      создавать пользователя можно только в свой отдел, только администратор может в любой отдел, приоритет роли нового пользователя не должен быть >= вашего
     */
    @Transactional
    public String createUser(CreateUserCommand command, String authorId, int authorRolePriority,
                             Collection<AuthorityType> authorAuthorities) {
        validationUtil.checkIfEmailWithinLimit(command.getEmail());
        validationUtil.checkThatUserDoesntExist(command.getEmail());

        User resourceManager = validationUtil.findUserIfExist(command.getResourceManagerId());
        validationUtil.checkIfOtherRolePriorityHigherThanAuthors(resourceManager.getRoleId(), authorRolePriority, authorId);
        Role role = validationUtil.findRoleIfExist(command.getRoleId());
        Department department = validationUtil.findDepartmentIfExist(command.getDepartmentId());
        City city = validationUtil.findCityIfExist(command.getCityId());
        Country country = validationUtil.findCountryIfExist(city.getCountryId());
        validationUtil.checkIfAllowedToCreateUserWithThisRole(role.getPriority(), authorRolePriority);
        Set<Activity> activities = validationUtil.findUserActivities(command.getActivitiesIds());
        boolean isRm = validationUtil.isRmRole(role);

        validationUtil.checkPermissionsForManipulatingUser(authorId, authorAuthorities, authorRolePriority,
                command.getRoleId(), command.getDepartmentId());

        UserCreatedEvent.Payload payload = UserCreatedEvent.Payload.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .email(command.getEmail())
                .avatarPath(command.getAvatarPath())
                .roleId(command.getRoleId())
                .roleName(role.getName())
                .cityId(city.getId())
                .cityName(city.getName())
                .countryId(country.getId())
                .countryName(country.getName())
                .departmentId(department.getId())
                .departmentName(department.getName())
                .resourceManagerId(resourceManager.getId())
                .resourceManagerFirstName(resourceManager.getFirstName())
                .resourceManagerLastName(resourceManager.getLastName())
                .activities(subordinateTreeUtil.activitiesToMap(activities))
                .isRm(isRm)
                .build();

        UserCreatedEvent event = new UserCreatedEvent();
        event.setPayload(payload);
        eventUtil.populateEventFields(event, UUID.randomUUID().toString(), 1, authorId, payload, true);

        eventPublisher.publishNoReupload(event);

        User user = userProjector.project(event);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * @param userId             id изменяемого пользователя
     * @param command            команда на создание пользователя
     * @param authorId           id пользователя, который делает данную операцию
     * @param authorRolePriority приоритет роли пользователя, который делает данную операцию
     * @param authorAuthorities  список прав пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.UserNotFoundException                       пользователь или RM с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.conflict.UserAlreadyExistException                   пользователь с таким email уже существует
     * @throws com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException                 отдел с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.notfound.ActivityNotFoundException                   активности не найдены
     * @throws com.rm.toolkit.user.command.exception.notfound.CityNotFoundException                       город с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException приоритет роли должен быть в диапазное 1<x<ваш приоритет
     * @throws UserHasDepartmentsException                                                                глав отделов нельзя удалять или переводить в другой отдел, не сняв с них прежде роль главы отдлела
     * @throws AccessDeniedException                                                                      менять отдел пользователя могут только глава отдела, в который пользователь отправляется, либо администратор, приоритет роли измененного пользователя не должен быть >= вашего
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void editUser(String userId, EditUserCommand command, String authorId, int authorRolePriority,
                         Collection<AuthorityType> authorAuthorities) {
        validationUtil.checkIfEmailWithinLimit(command.getEmail());
        User user = validationUtil.findUserIfExist(userId);

        if (!command.getEmail().equals(user.getEmail())) {
            validationUtil.checkThatUserDoesntExist(command.getEmail());
        }
        validationUtil.checkIfOtherRolePriorityHigherOrEqualsThanAuthors(user.getRoleId(), authorRolePriority, authorId);

        User resourceManager = validationUtil.findUserIfExist(command.getResourceManagerId());
        validationUtil.checkIfOtherRolePriorityHigherThanAuthors(resourceManager.getRoleId(), authorRolePriority, authorId);
        Role role = validationUtil.findRoleIfExist(command.getRoleId());
        Department department = validationUtil.findDepartmentIfExist(command.getDepartmentId());
        City city = validationUtil.findCityIfExist(command.getCityId());
        Country country = validationUtil.findCountryIfExist(city.getCountryId());
        Set<Activity> activities = validationUtil.findUserActivities(command.getActivitiesIds());
        validationUtil.checkIfAllowedToCreateUserWithThisRole(role.getPriority(), authorRolePriority);
        boolean isRm = validationUtil.isRmRole(role);


        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String userEditedEventId = UUID.randomUUID().toString();

        if (validationUtil.isRm(role) && (!user.getDepartmentId().equals(command.getDepartmentId())
                || (StatusType.DELETED.equals(command.getStatus())))) {
            validationUtil.userShouldNotBeDepartmentHead(user);
            subordinateTreeUtil.addToQueueEventsToMoveUsersUp(eventQueue, List.of(user), authorId, userEditedEventId);
        }

        validationUtil.checkPermissionsForManipulatingUser(authorId, authorAuthorities, authorRolePriority,
                command.getRoleId(), command.getDepartmentId());

        UserEditedEvent.Payload payload = UserEditedEvent.Payload.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .email(command.getEmail())
                .avatarPath(command.getAvatarPath())
                .roleId(command.getRoleId())
                .roleName(role.getName())
                .cityId(city.getId())
                .cityName(city.getName())
                .countryId(country.getId())
                .countryName(country.getName())
                .departmentId(department.getId())
                .departmentName(department.getName())
                .resourceManagerId(resourceManager.getId())
                .resourceManagerFirstName(resourceManager.getFirstName())
                .resourceManagerLastName(resourceManager.getLastName())
                //Оставляем старый статус пользователя. Для изменения статуса есть отдельный endpoint.
                .status(user.getStatus())
                .activities(subordinateTreeUtil.activitiesToMap(activities))
                .isRm(isRm)
                .build();

        UserEditedEvent event = new UserEditedEvent();
        event.setId(userEditedEventId);
        eventUtil.populateEventFields(event, user.getId(), user.getVersion() + 1, authorId, payload, false);
        eventQueue.add(event);

        // Если имя изменилось, то проекцию юзера на user-query-service тоже нужно изменить
        if (!command.getFirstName().equals(user.getFirstName()) || !command.getLastName().equals(user.getLastName())) {
            for (User subordinate : userRepository.findAllByResourceManagerId(user.getId())) {
                UserRmChangedEvent.Payload userRmChangedPayload = UserRmChangedEvent.Payload.builder()
                        .resourceManagerId(user.getId())
                        .resourceManagerFirstName(command.getFirstName())
                        .resourceManagerLastName(command.getLastName())
                        .build();
                UserRmChangedEvent userRmChangedEvent = new UserRmChangedEvent();
                eventUtil.populateEventFields(userRmChangedEvent, subordinate.getId(), user.getVersion() + 1,
                                authorId, userRmChangedPayload
                                , true);
                eventQueue.add(userRmChangedEvent);
            }
        }
        userProjector.project(event, user);
        userRepository.save(user);

        eventPublisher.publish(eventQueue);
    }


    /**
     * @param userId            id изменяемого пользователя
     * @param command           команда с id нового отдела
     * @param authorId          id пользователя, который делает данную операцию
     * @param authorAuthorities список прав пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.UserNotFoundException       пользователь с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException отдел с таким id не найден
     * @throws UserHasDepartmentsException                                                глав отделов нельзя переводить в другой отдел, не сняв с них прежде роль главы отдлела
     * @throws AccessDeniedException                                                      менять отдел пользователя могут только глава отдела, в который пользователь отправляется, либо администратор
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeUserDepartment(String userId, ChangeUserDepartmentCommand command, String authorId,
                                     Collection<AuthorityType> authorAuthorities) {
        User user = validationUtil.findUserIfExist(userId);
        validationUtil.userShouldNotBeDepartmentHead(user);
        Role role = validationUtil.findRoleIfExist(user.getRoleId());

        Department department = validationUtil.findDepartmentIfExist(command.getDepartmentId());
        if (command.getDepartmentId().equals(user.getDepartmentId())) return;

        User resourceManager = validationUtil.findUserIfExist(department.getHeadId());

        // Проверка, что только глава отдела или Admin изменяют отдел
        if (!validationUtil.isAdmin(authorAuthorities)
                && !resourceManager.getId().equals(authorId)) {
            throw new AccessDeniedException("Редактировать отдел могут только Админ и глава отдела");
        }


        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String userDepartmentChangedEventId = UUID.randomUUID().toString();

        if (validationUtil.isRm(role)) {
            validationUtil.userShouldNotBeDepartmentHead(user);
            subordinateTreeUtil.addToQueueEventsToMoveUsersUp(eventQueue, List.of(user), authorId,
                    userDepartmentChangedEventId);
        }

        UserDepartmentChangedEvent event = new UserDepartmentChangedEvent();
        UserDepartmentChangedEvent.Payload payload = UserDepartmentChangedEvent.Payload.builder()
                .departmentId(department.getId())
                .departmentName(department.getName())
                .resourceManagerId(resourceManager.getId())
                .resourceManagerFirstName(resourceManager.getFirstName())
                .resourceManagerLastName(resourceManager.getLastName())
                .build();
        event.setId(userDepartmentChangedEventId);
        eventUtil.populateEventFields(event, userId, user.getVersion() + 1, authorId, payload, false);
        eventQueue.add(event);

        userProjector.project(event, user);
        userRepository.save(user);

        eventPublisher.publish(eventQueue);
    }

    /**
     * @param userId            id изменяемого пользователя
     * @param newStatus         новый статус
     * @param authorId          id пользователя, который делает данную операцию
     * @param authorAuthorities список прав пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.UserNotFoundException пользователь с таким id не найден
     * @throws AccessDeniedException                                                статус пытается изменить не глава отдело или администратор
     * @throws UserHasDepartmentsException                                          глав отделов нельзя удалить, не сняв с них прежде роль главы отдлела
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeUserStatus(String userId, StatusType newStatus, String authorId,
                                 Collection<AuthorityType> authorAuthorities) {
        User user = validationUtil.findUserIfExist(userId);
        Role role = validationUtil.findRoleIfExist(user.getRoleId());
        Department department = validationUtil.findDepartmentIfExist(user.getDepartmentId());

        // Проверка, что только глава отдела или Admin изменяют статус
        if (!validationUtil.isAdmin(authorAuthorities)
                && !department.getHeadId().equals(authorId)) {
            throw new AccessDeniedException("Редактировать статус могут только Админ и глава отдела");
        }


        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String userStatusChangedEventId = UUID.randomUUID().toString();

        if (StatusType.DELETED.equals(newStatus) && validationUtil.isRm(role)) {
            validationUtil.userShouldNotBeDepartmentHead(user);
            subordinateTreeUtil.addToQueueEventsToMoveUsersUp(eventQueue, List.of(user), authorId, userStatusChangedEventId);
        }

        UserStatusChangedEvent event = new UserStatusChangedEvent();
        UserStatusChangedEvent.Payload payload = UserStatusChangedEvent.Payload.builder().status(newStatus).build();
        event.setId(userStatusChangedEventId);
        eventUtil.populateEventFields(event, user.getId(), user.getVersion() + 1, authorId, payload,
                false, userStatusChangedEventId);
        eventQueue.add(event);

        userProjector.project(event, user);
        userRepository.save(user);

        eventPublisher.publish(eventQueue);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeUsersDepartment(String departmentId, ChangeUsersDepartmentCommand command, String authorId,
                                      Collection<AuthorityType> authorAuthorities) {

        Department department = validationUtil.findDepartmentIfExist(departmentId);
        User resourceManager = validationUtil.findUserIfExist(department.getHeadId());

        if (!validationUtil.isAdmin(authorAuthorities) && !resourceManager.getId().equals(authorId)) {
            throw new AccessDeniedException("Редактировать отдел могут только Админ и глава отдела");
        }



        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String userStatusChangedEventId = UUID.randomUUID().toString();

        Set<String> newEmployee = command.getEmployees();

        /**
         * вытаскиваем всех сотрудников с нужным departmentId
         * меняем отдел этих сотрудников на нужный
         */
        Set<User> employees = userRepository.findUsersById(newEmployee);
        if (employees.size()!= newEmployee.size()){
            throw new UserNotFoundException("Список содержит ноу неймов");
        }
        employees.forEach(user -> user.setDepartmentId(departmentId));
        employees.forEach(user -> user.setResourceManagerId(department.getHeadId()));
        userRepository.saveAll(employees);

        /**
         * отправляем ивент по изменениям
         */
        for (User user : employees) {
            ChangeUsersDepartmentEvent event = new ChangeUsersDepartmentEvent();
            ChangeUsersDepartmentEvent.Payload payload = ChangeUsersDepartmentEvent.Payload.builder()
                    .departmentId(departmentId)
                    .departmentName(department.getName())
                    .resourceManagerId(resourceManager.getId())
                    .resourceManagerFirstName(resourceManager.getFirstName())
                    .resourceManagerLastName(resourceManager.getLastName())
                    .users(newEmployee)
                    .build();

            event.setId(userStatusChangedEventId);

            eventUtil.populateEventFields(event, user.getId(), user.getVersion() + 1, authorId, payload, false);
            eventQueue.add(event);

            userProjector.project(event, user);
            eventPublisher.publish(eventQueue);
        }
    }
}
