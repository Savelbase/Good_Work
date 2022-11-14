package com.rm.toolkit.user.command.service;

import com.rm.toolkit.user.command.dto.command.user.ChangeUserDepartmentCommand;
import com.rm.toolkit.user.command.dto.command.user.CreateUserCommand;
import com.rm.toolkit.user.command.dto.command.user.EditUserCommand;
import com.rm.toolkit.user.command.event.user.UserCreatedEvent;
import com.rm.toolkit.user.command.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.user.command.event.user.UserEditedEvent;
import com.rm.toolkit.user.command.event.user.UserStatusChangedEvent;
import com.rm.toolkit.user.command.message.EventPublisher;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.*;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.repository.UserRepository;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.testUtil.TestModelsBuilder;
import com.rm.toolkit.user.command.util.EventUtil;
import com.rm.toolkit.user.command.util.SubordinateTreeUtil;
import com.rm.toolkit.user.command.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserCommandService.class, TestModelsBuilder.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(UserProjector.class), @MockBean(UserRepository.class),
        @MockBean(EventPublisher.class), @MockBean(SubordinateTreeUtil.class),
        @MockBean(ValidationUtil.class), @MockBean(EventUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserCommandServiceTest {
    private final UserCommandService userCommandService;
    private final TestModelsBuilder modelsBuilder;

    private final UserRepository userRepository;
    private final UserProjector userProjector;
    private final ValidationUtil validationUtil;

    private final String authorId = "Test";
    private final String userId = "Test";
    private final Collection<AuthorityType> authorAuthorities = new HashSet<>();

    private final Supplier<CreateUserCommand> createUserCommandSupplier = () -> {
        String value = "Test";
        CreateUserCommand command = new CreateUserCommand();
        command.setDepartmentId(value);
        command.setEmail(value);
        command.setAvatarPath(value);
        command.setCityId(value);
        command.setRoleId(value);
        command.setFirstName(value);
        command.setLastName(value);
        command.setResourceManagerId(value);
        command.setActivitiesIds(new HashSet<>());
        return command;
    };

    private final Supplier<EditUserCommand> editUserCommandSupplier = () -> {
        String value = "Test";
        EditUserCommand command = new EditUserCommand();
        command.setDepartmentId(value);
        command.setEmail(value);
        command.setAvatarPath(value);
        command.setCityId(value);
        command.setRoleId(value);
        command.setFirstName(value);
        command.setLastName(value);
        command.setResourceManagerId(value);
        command.setActivitiesIds(new HashSet<>());
        return command;
    };

    @Test
    void createUser() {
        CreateUserCommand command = createUserCommandSupplier.get();
        int authorRolePriority = 0;
        User user = modelsBuilder.getTestUser();
        Role role = modelsBuilder.getTestRole();
        City city = modelsBuilder.getTestCity();
        Country country = modelsBuilder.getTestCountry();
        Department department = modelsBuilder.getTestDepartment();

        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);
        when(validationUtil.findCityIfExist(anyString())).thenReturn(city);
        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);
        when(validationUtil.findCountryIfExist(anyString())).thenReturn(country);
        when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);
        when(userProjector.project(any(UserCreatedEvent.class))).thenReturn(user);

        userCommandService.createUser(command, authorId, authorRolePriority, authorAuthorities);

        verify(validationUtil, times(1)).checkIfEmailWithinLimit(isA(String.class));
        verify(validationUtil, times(1)).checkThatUserDoesntExist(isA(String.class));
        verify(validationUtil, times(1)).checkIfOtherRolePriorityHigherThanAuthors(isA(String.class), isA(Integer.class), isA(String.class));
        verify(userProjector, times(1)).project(isA(UserCreatedEvent.class));
        verify(userRepository, times(1)).save(isA(User.class));
    }

    @Test
    void editUser() {
        EditUserCommand command = editUserCommandSupplier.get();
        command.setEmail("Random name");
        int authorRolePriority = 0;
        User user = modelsBuilder.getTestUser();
        Role role = modelsBuilder.getTestRole();
        City city = modelsBuilder.getTestCity();
        Country country = modelsBuilder.getTestCountry();
        Department department = modelsBuilder.getTestDepartment();

        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);
        when(validationUtil.findCityIfExist(anyString())).thenReturn(city);
        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);
        when(validationUtil.findCountryIfExist(anyString())).thenReturn(country);
        when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);

        userCommandService.editUser(userId, command, authorId, authorRolePriority, authorAuthorities);

        verify(validationUtil, times(1)).checkIfEmailWithinLimit(isA(String.class));
        verify(validationUtil, times(1)).checkThatUserDoesntExist(isA(String.class));
        verify(validationUtil, times(1)).checkIfOtherRolePriorityHigherThanAuthors(isA(String.class), isA(Integer.class), isA(String.class));
        verify(userProjector, times(1)).project(isA(UserEditedEvent.class), isA(User.class));
        verify(userRepository, times(1)).save(isA(User.class));
    }

    @Test
    void changeUserDepartment() {
        ChangeUserDepartmentCommand command = new ChangeUserDepartmentCommand();
        command.setDepartmentId("Random name");
        User user = modelsBuilder.getTestUser();
        Department department = modelsBuilder.getTestDepartment();

        when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);
        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);

        userCommandService.changeUserDepartment(userId, command, authorId, authorAuthorities);

        verify(userProjector, times(1)).project(isA(UserDepartmentChangedEvent.class), isA(User.class));
        verify(userRepository, times(1)).save(isA(User.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhileChangeUserDepartment() {
        ChangeUserDepartmentCommand command = new ChangeUserDepartmentCommand();
        command.setDepartmentId("Random name");
        User user = modelsBuilder.getTestUser();
        user.setId("Not head Id");
        Department department = modelsBuilder.getTestDepartment();

        when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);
        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);

        assertThrows(AccessDeniedException.class, () -> userCommandService.changeUserDepartment(userId, command, authorId, authorAuthorities));
    }

    @Test
    void shouldDoNothingIfChangeDepartmentToCurrentOne() {
        ChangeUserDepartmentCommand command = new ChangeUserDepartmentCommand();
        command.setDepartmentId("Test");
        User user = modelsBuilder.getTestUser();

        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);

        userCommandService.changeUserDepartment(userId, command, authorId, authorAuthorities);

        verify(userProjector, times(0)).project(isA(UserDepartmentChangedEvent.class), isA(User.class));
        verify(userRepository, times(0)).save(isA(User.class));
    }

    @Test
    void changeUserStatus() {
        StatusType type = StatusType.BLOCKED;
        User user = modelsBuilder.getTestUser();
        Department department = modelsBuilder.getTestDepartment();

        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);
        when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);

        userCommandService.changeUserStatus(userId, type, authorId, authorAuthorities);

        verify(userProjector, times(1)).project(isA(UserStatusChangedEvent.class), isA(User.class));
        verify(userRepository, times(1)).save(isA(User.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhileChangeUserStatus() {
        StatusType type = StatusType.BLOCKED;
        User user = modelsBuilder.getTestUser();
        Department department = modelsBuilder.getTestDepartment();
        String currentTestAuthorId = "Not head ID";

        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);
        when(validationUtil.findDepartmentIfExist(anyString())).thenReturn(department);

        assertThrows(AccessDeniedException.class, () -> userCommandService.changeUserStatus(userId, type, currentTestAuthorId, authorAuthorities));
    }
}