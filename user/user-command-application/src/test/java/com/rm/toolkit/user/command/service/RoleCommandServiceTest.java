package com.rm.toolkit.user.command.service;

import com.rm.toolkit.user.command.dto.command.role.CreateRoleCommand;
import com.rm.toolkit.user.command.dto.command.role.EditRoleCommand;
import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.role.RoleCreatedEvent;
import com.rm.toolkit.user.command.event.role.RoleDeletedEvent;
import com.rm.toolkit.user.command.event.role.RoleEditedEvent;
import com.rm.toolkit.user.command.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.user.command.event.user.UserRoleChangedEvent;
import com.rm.toolkit.user.command.exception.conflict.UserHasDepartmentsException;
import com.rm.toolkit.user.command.exception.unprocessableentity.AdminRoleIsNotEditableException;
import com.rm.toolkit.user.command.exception.unprocessableentity.BaseRoleNotEditableException;
import com.rm.toolkit.user.command.message.EventPublisher;
import com.rm.toolkit.user.command.message.projector.RoleProjector;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.RoleRepository;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.testUtil.TestModelsBuilder;
import com.rm.toolkit.user.command.util.EventUtil;
import com.rm.toolkit.user.command.util.InvocationServicesUtil;
import com.rm.toolkit.user.command.util.SubordinateTreeUtil;
import com.rm.toolkit.user.command.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RoleCommandService.class, TestModelsBuilder.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(RoleProjector.class), @MockBean(RoleRepository.class),
        @MockBean(UserProjector.class), @MockBean(UserRepository.class),
        @MockBean(EventPublisher.class), @MockBean(SubordinateTreeUtil.class),
        @MockBean(ValidationUtil.class), @MockBean(EventUtil.class),
        @MockBean(InvocationServicesUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RoleCommandServiceTest {
    private final RoleCommandService roleCommandService;

    private final RoleProjector roleProjector;
    private final UserProjector userProjector;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final EventPublisher eventPublisher;
    private final EventUtil eventUtil;

    private final TestModelsBuilder modelsBuilder;

    private final String authorId = "Test";
    private final String roleId = "Test";

    @Test
    @SuppressWarnings("unchecked")
    void createRole() {
        CreateRoleCommand command = new CreateRoleCommand();
        command.setName("Test");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        Role role = modelsBuilder.getTestRole();

        when(roleProjector.project(any(RoleCreatedEvent.class))).thenReturn(role);

        roleCommandService.createRole(command, authorId);

        verify(validationUtil, times(1)).checkIfRoleNameWithinLimit(anyString());
        verify(validationUtil, times(1)).checkThatRoleDoesntExist(anyString());
        verify(validationUtil, times(1)).checkIfRolePriorityWithinBounds(anyInt());
        verify(validationUtil, times(1)).validateAuthorities(any(Set.class));
        verify(eventPublisher, times(1)).publishNoReupload(any(RoleCreatedEvent.class));
        verify(roleProjector, times(1)).project(any(RoleCreatedEvent.class));
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void editRole() {
        EditRoleCommand command = new EditRoleCommand();
        command.setName("Random name");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        Role role = modelsBuilder.getTestRole();

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);
        when(roleProjector.project(any(RoleCreatedEvent.class))).thenReturn(role);

        roleCommandService.editRole(roleId, command, authorId);

        verify(validationUtil, times(1)).checkIfRoleNameWithinLimit(anyString());
        verify(validationUtil, times(1)).checkThatRoleDoesntExist(anyString());
        verify(validationUtil, times(1)).checkIfRolePriorityWithinBounds(anyInt());
        verify(validationUtil, times(1)).validateAuthorities(any(Set.class));
        verify(eventPublisher, times(1)).publish(any(Queue.class));
        verify(roleProjector, times(1)).project(any(RoleEditedEvent.class), any(Role.class));
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void shouldThrowAdminRoleIsNotEditableExceptionWhileEditRole() {
        EditRoleCommand command = new EditRoleCommand();
        command.setName("Test");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        Role role = modelsBuilder.getTestRole();

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);
        when(validationUtil.isAdmin(any(Role.class))).thenReturn(true);

        assertThrows(AdminRoleIsNotEditableException.class, () -> roleCommandService.editRole(roleId, command, authorId));
    }

    @Test
    void shouldThrowBaseRoleNotEditableExceptionWhileEditRole() {
        EditRoleCommand command = new EditRoleCommand();
        command.setName("Test");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        Role role = modelsBuilder.getTestRole();
        role.setImmutable(true);

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);

        assertThrows(BaseRoleNotEditableException.class, () -> roleCommandService.editRole(roleId, command, authorId));
    }

    @Test
    void shouldThrowUserHasDepartmentsExceptionWhileEditRole() {
        EditRoleCommand command = new EditRoleCommand();
        command.setName("Test");
        command.setPriority(0);
        command.setAuthorities(new HashSet<>());
        Role role = modelsBuilder.getTestRole();
        Set<User> heads = new HashSet<>();
        heads.add(modelsBuilder.getTestUser());

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);
        when(validationUtil.isDepartmentHead(any(Role.class))).thenReturn(true);
        when(userRepository.findHeadsWithRoleId(anyString())).thenReturn(heads);

        assertThrows(UserHasDepartmentsException.class, () -> roleCommandService.editRole(roleId, command, authorId));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deleteRole() {
        Role role = modelsBuilder.getTestRole();
        User author = modelsBuilder.getTestUser();
        Set<User> usersWithThisRole = new HashSet<>();
        usersWithThisRole.add(modelsBuilder.getTestUser());
        Role employeeRole = modelsBuilder.getTestRole();

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role).thenReturn(employeeRole);
        when(validationUtil.findUserIfExist(anyString())).thenReturn(author);
        when(userRepository.findAllByRoleId(anyString())).thenReturn(usersWithThisRole);
        when(roleProjector.project(any(RoleCreatedEvent.class))).thenReturn(role);
        doNothing().when(eventUtil).populateEventFields(any(UserDepartmentChangedEvent.class), anyString(), anyInt(),
                anyString(), any(UserDepartmentChangedEvent.Payload.class), anyString());

        roleCommandService.deleteRole(roleId, authorId);

        verify(eventPublisher, times(1)).publish(any(Queue.class));
        verify(roleProjector, times(1)).project(any(RoleDeletedEvent.class), any(Role.class));
        verify(roleRepository, times(1)).delete(any(Role.class));
    }

    @Test
    void addToQueueUserRoleChangedEvents() {
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        User user = modelsBuilder.getTestUser();
        Role role = modelsBuilder.getTestRole();
        String parentEventId = "Test";
        User author = modelsBuilder.getTestUser();

        roleCommandService.addToQueueUserRoleChangedEvents(eventQueue, user, role, parentEventId, author);

        verify(eventUtil, times(1)).populateEventFields(any(UserRoleChangedEvent.class),
                anyString(), anyInt(), anyString(), any(UserRoleChangedEvent.Payload.class), anyBoolean(), anyString());
        verify(userProjector, times(1)).project(any(UserRoleChangedEvent.class), any(User.class));
        assertFalse(eventQueue.isEmpty());
    }
}