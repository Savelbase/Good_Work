package com.rm.toolkit.user.command.service;

import com.rm.toolkit.user.command.dto.command.department.CreateDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.department.EditDepartmentCommand;
import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.command.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.user.command.event.user.UserRmChangedEvent;
import com.rm.toolkit.user.command.exception.conflict.UserCantBecomeDepartmentHeadException;
import com.rm.toolkit.user.command.message.EventPublisher;
import com.rm.toolkit.user.command.message.projector.DepartmentProjector;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.Department;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.DepartmentRepository;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.util.EventUtil;
import com.rm.toolkit.user.command.util.InvocationServicesUtil;
import com.rm.toolkit.user.command.util.SubordinateTreeUtil;
import com.rm.toolkit.user.command.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentCommandService {

    private final DepartmentProjector departmentProjector;
    private final DepartmentRepository departmentRepository;
    private final UserProjector userProjector;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final SubordinateTreeUtil subordinateTreeUtil;
    private final ValidationUtil validationUtil;
    private final EventUtil eventUtil;
    private final InvocationServicesUtil invocationServicesUtil;

    @Value("${department.empty}")
    private String emptyDepartmentId;

    /**
     * @param command  название отдела и список членов
     * @param authorId id пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.conflict.DepartmentAlreadyExistsException отдел с таким именем уже существует
     * @throws UserCantBecomeDepartmentHeadException                                           пользователь не имеет достаточных прав, чтобы стать главой отдела
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String createDepartment(CreateDepartmentCommand command, String authorId) {
        validationUtil.checkIfDepartmentNameWithinLimit(command.getName());
        validationUtil.checkThatDepartmentDoesntExist(command.getName());

        User head = validationUtil.findUserIfExist(command.getHeadId());
        validationUtil.checkIfCanBecomeNewDepartmentHead(head);

        User author = validationUtil.findUserIfExist(authorId);

        command.getMembersIds().remove(head.getId());
        Set<User> members = command.getMembersIds().stream().map(validationUtil::findUserIfExist)
                .collect(Collectors.toSet());

        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String departmentCreatedEventId = UUID.randomUUID().toString();

        subordinateTreeUtil.addToQueueEventsToMoveUsersUp(eventQueue, members, authorId, departmentCreatedEventId);

        String departmentId = UUID.randomUUID().toString();

        DepartmentCreatedEvent event = new DepartmentCreatedEvent();
        DepartmentCreatedEvent.Payload payload = DepartmentCreatedEvent.Payload.builder()
                .name(command.getName())
                .headId(command.getHeadId())
                .headFirstName(head.getFirstName())
                .headLastName(head.getLastName())
                .deletable(true)
                .build();
        event.setId(departmentCreatedEventId);
        eventUtil.populateEventFields(event, departmentId, 1, authorId, payload, false);
        eventQueue.add(event);
        Department department = departmentProjector.project(event);
        departmentRepository.save(department);

        addToQueueUserDepartmentChangedEvents(eventQueue, head, department, author, departmentCreatedEventId, author);
        addUserInDepartment(eventQueue, members, department, departmentCreatedEventId, author);
        invocationServicesUtil.changeRolesByDepartmentService(eventQueue, head, departmentCreatedEventId, author);

        eventPublisher.publish(eventQueue);
        return department.getId();
    }

    /**
     * @param departmentId id изменяемого отдела
     * @param command      новое название отдела и список членов
     * @param authorId     id пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException      отдел с таким id не найден
     * @throws com.rm.toolkit.user.command.exception.conflict.DepartmentAlreadyExistsException отдел с таким именем уже существует
     * @throws UserCantBecomeDepartmentHeadException                                           пользователь не имеет достаточных прав, чтобы стать главой отдела
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void editDepartment(String departmentId, EditDepartmentCommand command, String authorId) {
        validationUtil.checkIfDepartmentNameWithinLimit(command.getName());
        Department department = validationUtil.findDepartmentIfExist(departmentId);

        if (!command.getName().equals(department.getName())) {
            validationUtil.checkThatDepartmentDoesntExist(command.getName());
        }
        User oldHead = validationUtil.findUserIfExist(department.getHeadId());
        User newHead = validationUtil.findUserIfExist(command.getHeadId());
        User author = validationUtil.findUserIfExist(authorId);

        Department oldDepartment = departmentRepository.findDepartmentById(departmentId);

        if (!command.getName().equals(oldDepartment.getName()) || !command.getHeadId().equals(oldDepartment.getHeadId())) {

            Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
            String departmentEditedEventId = UUID.randomUUID().toString();

            if (!command.getHeadId().equals(oldDepartment.getHeadId())) {
                /**
                 * смена главы отдела в нашей бд
                 */
                Set<User> oldMembers = userRepository.findAllByDepartmentId(departmentId);
                oldMembers.forEach(user -> user.setResourceManagerId(command.getHeadId()));
                userRepository.saveAll(oldMembers);
                /**
                 * отправка ивента о смене главы отдела в query сервис
                 */
                for (User nonMovedMember : oldMembers) {
                    UserDepartmentChangedEvent userDepartmentChangedEvent = new UserDepartmentChangedEvent();
                    UserDepartmentChangedEvent.Payload userDepartmentChangedPayload = UserDepartmentChangedEvent.Payload
                            .builder()
                            .departmentId(departmentId)
                            .departmentName(command.getName())
                            .resourceManagerId(command.getHeadId())
                            .resourceManagerFirstName(newHead.getFirstName())
                            .resourceManagerLastName(newHead.getLastName())
                            .build();
                    userDepartmentChangedEvent.setId(departmentEditedEventId);
                    eventUtil.populateEventFields(userDepartmentChangedEvent, nonMovedMember.getId(),
                            nonMovedMember.getVersion() + 1, authorId, userDepartmentChangedPayload,
                            departmentEditedEventId);
                    eventQueue.add(userDepartmentChangedEvent);
                    userProjector.project(userDepartmentChangedEvent, nonMovedMember);
                }
            }
            /**
             * отправка ивента о смене названия отдела в query сервис
             */
            DepartmentEditedEvent event = new DepartmentEditedEvent();
            DepartmentEditedEvent.Payload payload = DepartmentEditedEvent.Payload.builder()
                    .name(command.getName())
                    .headId(command.getHeadId())
                    .headFirstName(newHead.getFirstName())
                    .headLastName(newHead.getLastName())
                    .build();
            event.setId(departmentEditedEventId);
            eventUtil.populateEventFields(event, departmentId, department.getVersion() + 1, authorId, payload,
                    false);
            eventQueue.add(event);
            departmentProjector.project(event, department);
            /**
             * сохранение нового имени отдела
             */
            departmentRepository.save(department);

            invocationServicesUtil.changeRolesByDepartmentService(eventQueue, newHead, oldHead, departmentEditedEventId, author);

            eventPublisher.publish(eventQueue);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void addUserInDepartment(Queue<Event<? extends EventPayload>> eventQueue, Set<User> membersAdded, Department department, String departmentEditedEventId, User author) {
        // Добавление пользователей в отдел
        addToQueueChangeUserDepartmentEvent(eventQueue, membersAdded, department, departmentEditedEventId, author);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deletedUserFromDepartment(Set<User> membersRemoved, Queue<Event<? extends EventPayload>> eventQueue, String departmentEditedEventId, User author) {
        // Удаление пользователей из отдела
        if (!membersRemoved.isEmpty()) {
            Department emptyDepartment = validationUtil.findDepartmentIfExist(emptyDepartmentId);

            addUserInDepartment(eventQueue, membersRemoved, emptyDepartment, departmentEditedEventId, author);
        }
    }

    /**
     * @param departmentId id удаляемого отдела
     * @param authorId     id пользователя, который делает данную операцию
     * @throws com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException отдел с таким id не найден
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteDepartment(String departmentId, String authorId) {
        Department department = validationUtil.findDepartmentIfExist(departmentId);

        User author = validationUtil.findUserIfExist(authorId);

        Set<User> members = userRepository.findAllByDepartmentId(departmentId);

        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        String departmentDeletedEventId = UUID.randomUUID().toString();

        // Удаление пользователей из отдела
        deletedUserFromDepartment(members, eventQueue, departmentDeletedEventId, author);

        DepartmentDeletedEvent event = new DepartmentDeletedEvent();
        DepartmentDeletedEvent.Payload payload = new DepartmentDeletedEvent.Payload();
        event.setId(departmentDeletedEventId);
        eventUtil.populateEventFields(event, departmentId, department.getVersion() + 1, authorId, payload,
                false);
        eventQueue.add(event);

        departmentProjector.project(event, department);
        departmentRepository.save(department);

        eventPublisher.publish(eventQueue);
    }

    @Transactional
    public void addToQueueUserDepartmentChangedEvents(Queue<Event<? extends EventPayload>> eventQueue, User user,
                                                      Department department, User rm, String parentEventId,
                                                      User author) {
        UserDepartmentChangedEvent userDepartmentChangedEvent = new UserDepartmentChangedEvent();
        UserDepartmentChangedEvent.Payload headDepartmentChangedPayload = UserDepartmentChangedEvent.Payload.builder()
                .departmentId(department.getId())
                .departmentName(department.getName())
                .resourceManagerId(rm.getId())
                .resourceManagerFirstName(rm.getFirstName())
                .resourceManagerLastName(rm.getLastName())
                .build();
        eventUtil.populateEventFields(userDepartmentChangedEvent, user.getId(), user.getVersion() + 1,
                author.getId(), headDepartmentChangedPayload, parentEventId);
        eventQueue.add(userDepartmentChangedEvent);

        userProjector.project(userDepartmentChangedEvent, user);
    }

    @Transactional
    public void addToQueueChangeUserDepartmentEvent(Queue<Event<? extends EventPayload>> eventQueue, Set<User> users,
                                                    Department department, String parentEventId, User author) {
        User head = validationUtil.findUserIfExist(department.getHeadId());
        for (User user : users) {
            addToQueueUserDepartmentChangedEvents(eventQueue, user, department, head, parentEventId, author);
        }
    }
}
