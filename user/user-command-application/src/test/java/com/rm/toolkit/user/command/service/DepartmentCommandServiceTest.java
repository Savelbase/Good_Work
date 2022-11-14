package com.rm.toolkit.user.command.service;

import com.rm.toolkit.user.command.testUtil.TestModelsBuilder;
import com.rm.toolkit.user.command.dto.command.department.CreateDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.department.EditDepartmentCommand;
import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.command.event.user.UserDepartmentChangedEvent;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = {DepartmentCommandService.class, TestModelsBuilder.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(DepartmentProjector.class), @MockBean(DepartmentRepository.class),
        @MockBean(UserProjector.class), @MockBean(UserRepository.class),
        @MockBean(EventPublisher.class), @MockBean(SubordinateTreeUtil.class),
        @MockBean(ValidationUtil.class), @MockBean(EventUtil.class),
        @MockBean(InvocationServicesUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class DepartmentCommandServiceTest {
    private final DepartmentCommandService departmentCommandService;

    private final DepartmentProjector departmentProjector;
    private final DepartmentRepository departmentRepository;
    private final UserProjector userProjector;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final ValidationUtil validationUtil;
    private final EventUtil eventUtil;

    private final TestModelsBuilder modelsBuilder;

    private final String authorId = "Test";

    @Test
    void createDepartment() {
        CreateDepartmentCommand command = new CreateDepartmentCommand();
        command.setHeadId("Test");
        command.setName("Test");
        command.setMembersIds(new HashSet<>());
        Department department = new Department();
        User head = modelsBuilder.getTestUser();

        Mockito.when(validationUtil.findUserIfExist(command.getHeadId())).thenReturn(head);
        Mockito.when(departmentProjector.project(any(DepartmentCreatedEvent.class))).thenReturn(department);

        departmentCommandService.createDepartment(command, authorId);

        Mockito.verify(validationUtil, times(1)).checkIfDepartmentNameWithinLimit(command.getName());
        Mockito.verify(validationUtil, times(1)).checkThatDepartmentDoesntExist(command.getName());
        Mockito.verify(departmentProjector).project(isA(DepartmentCreatedEvent.class));
        Mockito.verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void editDepartment() {
        String departmentId = "Test";
        EditDepartmentCommand command = new EditDepartmentCommand();
        command.setHeadId("Test");
        command.setName("Test");
        Department department = modelsBuilder.getTestDepartment();
        department.setName("Random Name");
        Role role = modelsBuilder.getTestRole();
        User user = modelsBuilder.getTestUser();

        Mockito.when(validationUtil.findUserIfExist(anyString())).thenReturn(user);
        Mockito.when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);
        Mockito.when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);

        departmentCommandService.editDepartment(departmentId, command, authorId);

        Mockito.verify(validationUtil, times(1)).checkIfDepartmentNameWithinLimit(command.getName());
        Mockito.verify(validationUtil, times(1)).checkThatDepartmentDoesntExist(command.getName());
        Mockito.verify(departmentProjector).project(isA(DepartmentEditedEvent.class), isA(Department.class));
        Mockito.verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteDepartment() {
        String departmentId = "Test";
        Department department = modelsBuilder.getTestDepartment();
        Set<User> members = new HashSet<>();
        members.add(modelsBuilder.getTestUser());
        User head = modelsBuilder.getTestUser();

        Mockito.when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);
        Mockito.when(userRepository.findAllByDepartmentId(anyString())).thenReturn(members);
        Mockito.when(validationUtil.findUserIfExist(anyString())).thenReturn(head);
        Mockito.doNothing().when(departmentProjector).project(any(DepartmentDeletedEvent.class), any(Department.class));
        Mockito.when(departmentRepository.save(any(Department.class))).thenReturn(department);
        Mockito.doNothing().when(eventPublisher).publish(any(Queue.class));
        Mockito.doNothing().when(eventUtil).populateEventFields(any(DepartmentDeletedEvent.class), anyString(),
                anyInt(), anyString(), any(DepartmentDeletedEvent.Payload.class), anyBoolean());

        departmentCommandService.deleteDepartment(departmentId, authorId);

        Mockito.verify(departmentProjector, times(1)).project(isA(DepartmentDeletedEvent.class), isA(Department.class));
        Mockito.verify(departmentRepository, times(1)).save(isA(Department.class));
        Mockito.verify(eventPublisher, times(1)).publish(isA(Queue.class));
    }

    @Test
    void addToQueueUserDepartmentChangedEvents() {
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        User user = modelsBuilder.getTestUser();
        Department department = modelsBuilder.getTestDepartment();
        User rm = modelsBuilder.getTestUser();
        String parentEventId = "Test";
        User author = modelsBuilder.getTestUser();
        UserDepartmentChangedEvent event = new UserDepartmentChangedEvent();

        Mockito.doNothing().when(userProjector).project(event, user);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        departmentCommandService.addToQueueUserDepartmentChangedEvents(eventQueue, user, department, rm, parentEventId, author);

        Mockito.verify(eventUtil).populateEventFields(isA(UserDepartmentChangedEvent.class), anyString(),
                anyInt(), anyString(), isA(UserDepartmentChangedEvent.Payload.class), anyString());
        Mockito.verify(userProjector, times(1)).project(any(UserDepartmentChangedEvent.class), any(User.class));
        Mockito.verify(userRepository, times(1)).save(user);
    }

    @Test
    void addToQueueChangeUserDepartmentEvent() {
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        Set<User> users = new HashSet<>();
        users.add(modelsBuilder.getTestUser());
        Department department = modelsBuilder.getTestDepartment();
        department.setHeadId("Test");
        String parentEventId = "Test";
        User author = modelsBuilder.getTestUser();
        User head = modelsBuilder.getTestUser();

        Mockito.when(validationUtil.findUserIfExist(department.getHeadId())).thenReturn(head);

        departmentCommandService.addToQueueChangeUserDepartmentEvent(eventQueue, users, department, parentEventId, author);

        Mockito.verify(validationUtil).findUserIfExist(department.getHeadId());
    }
}