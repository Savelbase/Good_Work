package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.user.UserRmChangedEvent;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.Activity;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {SubordinateTreeUtil.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(UserRepository.class), @MockBean(UserProjector.class),
        @MockBean(ValidationUtil.class), @MockBean(EventUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SubordinateTreeUtilTest {
    private final SubordinateTreeUtil subordinateTreeUtil;

    private final UserRepository userRepository;
    private final UserProjector userProjector;
    private final ValidationUtil validationUtil;
    private final EventUtil eventUtil;

    private final Supplier<User> userSupplier = () -> User.builder()
            .id("Test")
            .version(0)
            .status(StatusType.ACTIVE)
            .email("Test")
            .departmentId("Test")
            .roleId("Test")
            .resourceManagerId("Test")
            .lastName("Test")
            .firstName("Test")
            .build();

    @Test
    void activitiesToMap() {
        Set<Activity> activities = new HashSet<>();
        Activity activity = Activity.builder().id("Test").name("Test").build();
        activities.add(activity);

        Map<String, String> result = subordinateTreeUtil.activitiesToMap(activities);

        assertFalse(result.isEmpty());
    }

    @Test
    void getDirectSubordinates() {
        User rm = userSupplier.get();
        Collection<User> currentlyRemovedUsers = new HashSet<>();
        Map<User, User> changedRms = new HashMap<>();
        changedRms.put(new User(), rm);

        Collection<User> subordinates = subordinateTreeUtil.getDirectSubordinates(rm, currentlyRemovedUsers, changedRms);

        assertFalse(subordinates.isEmpty());
    }

    @Test
    void testGetDirectSubordinates() {
        User rm = userSupplier.get();
        Collection<User> currentlyRemovedUsers = new HashSet<>();
        Map<User, User> changedRms = new HashMap<>();
        changedRms.put(new User(), rm);
        int minRolePriority = 1, maxRolePriority = 10;

        Collection<User> subordinates = subordinateTreeUtil.getDirectSubordinates(rm, currentlyRemovedUsers, changedRms, minRolePriority, maxRolePriority);

        assertFalse(subordinates.isEmpty());
    }

    @Test
    void getAllSubordinates() {
        User rm = userSupplier.get();
        Set<User> users = new HashSet<>();
        User subordinate = new User();
        subordinate.setId("Random Id");
        users.add(subordinate);

        when(userRepository.findAllByResourceManagerId(any(String.class))).thenReturn(users).thenReturn(new HashSet<>());

        Collection<User> subordinates = subordinateTreeUtil.getAllSubordinates(rm);

        assertFalse(subordinates.isEmpty());
        verify(userRepository, times(2)).findAllByResourceManagerId(anyString());
    }

    @Test
    void getAllRmSubordinates() {
        User rm = userSupplier.get();
        User subordinate = userSupplier.get();
        Set<User> users = new HashSet<>();
        users.add(subordinate);
        Role role = new Role();

        when(userRepository.findAllByResourceManagerId(any(String.class))).thenReturn(users).thenReturn(new HashSet<>());
        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);
        when(validationUtil.isRm(any(Role.class))).thenReturn(true);

        Map<String, User> subordinates = subordinateTreeUtil.getAllRmSubordinates(rm);

        assertFalse(subordinates.isEmpty());
        verify(validationUtil).findRoleIfExist(isA(String.class));
        verify(validationUtil).isRm(isA(Role.class));
        verify(userRepository, times(2)).findAllByResourceManagerId(anyString());
    }

    @Test
    void addToQueueEventsToMoveUsersUp() {
        User user = userSupplier.get();
        Collection<User> removedUsersFromDepartment = new HashSet<>();
        removedUsersFromDepartment.add(user);
        User subordinate = userSupplier.get();
        subordinate.setId("Random id");
        Set<User> subordinates = new HashSet<>();
        subordinates.add(subordinate);
        String authorId = "Test", parentEventId = "Test";
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();

        when(userRepository.findAllByResourceManagerId(anyString())).thenReturn(subordinates);
        when(validationUtil.findUserIfExist(anyString())).thenReturn(user);

        subordinateTreeUtil.addToQueueEventsToMoveUsersUp(eventQueue, removedUsersFromDepartment, authorId ,parentEventId);

        verify(validationUtil).findUserIfExist(isA(String.class));
        assertFalse(eventQueue.isEmpty());
        verify(userProjector).project(isA(UserRmChangedEvent.class), isA(User.class));
    }

    @Test
    void returnWhatHappensWhenMoveUsersUp() {
        User user = userSupplier.get();
        Collection<User> removedUsersFromDepartment = new HashSet<>();
        removedUsersFromDepartment.add(user);
        User subordinate = userSupplier.get();
        subordinate.setId("Random id");
        Set<User> subordinates = new HashSet<>();
        subordinates.add(subordinate);

        when(userRepository.findAllByResourceManagerId(anyString())).thenReturn(subordinates);

        Map<User, User> changedRms = subordinateTreeUtil.returnWhatHappensWhenMoveUsersUp(removedUsersFromDepartment);

        assertFalse(changedRms.isEmpty());
        verify(validationUtil).findUserIfExist(isA(String.class));
    }

    @Test
    void testAddToQueueEventsToMoveUsersUp() {
        String authorId = "Test", parentEventId = "Test";
        User user = userSupplier.get();
        Map<User, User> changedRms = new HashMap<>();
        changedRms.put(user, user);
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();

        subordinateTreeUtil.addToQueueEventsToMoveUsersUp(eventQueue, changedRms, authorId, parentEventId);

        assertFalse(eventQueue.isEmpty());
        verify(userProjector).project(isA(UserRmChangedEvent.class), isA(User.class));
    }

    @Test
    void generateUserRmChangedEvent() {
        User user = userSupplier.get();
        User rm = userSupplier.get();
        String authorId = "Test", parentEventId = "Test";

        UserRmChangedEvent result = subordinateTreeUtil.generateUserRmChangedEvent(user, rm, authorId, parentEventId);

        assertNotNull(result);
        verify(eventUtil).populateEventFields(isA(UserRmChangedEvent.class), isA(String.class),
                isA(Integer.class), isA(String.class), isA(UserRmChangedEvent.Payload.class), isA(String.class));
    }
}