package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.service.RoleCommandService;
import com.rm.toolkit.user.command.testUtil.TestModelsBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {InvocationServicesUtil.class, TestModelsBuilder.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(RoleCommandService.class), @MockBean(ValidationUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class InvocationServicesUtilTest {
    private final InvocationServicesUtil invocationServicesUtil;
    private final TestModelsBuilder modelsBuilder;

    private final RoleCommandService roleCommandService;
    private final ValidationUtil validationUtil;

    @Test
    @SuppressWarnings("unchecked")
    void changeRolesByDepartmentService() {
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        User newHead = modelsBuilder.getTestUser();
        User author = modelsBuilder.getTestUser();
        Role role = modelsBuilder.getTestRole();
        String parentEventId = "Test";

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);

        invocationServicesUtil.changeRolesByDepartmentService(eventQueue, newHead, parentEventId, author);

        verify(roleCommandService, times(1)).addToQueueUserRoleChangedEvents(isA(Queue.class), isA(User.class), isA(Role.class), isA(String.class), isA(User.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void changeRolesByDepartmentServiceIncludingHead() {
        Queue<Event<? extends EventPayload>> eventQueue = new ArrayDeque<>();
        User oldHead = modelsBuilder.getTestUser();
        User newHead = modelsBuilder.getTestUser();
        User author = modelsBuilder.getTestUser();
        Role role = modelsBuilder.getTestRole();
        String parentEventId = "Test";

        when(validationUtil.findRoleIfExist(anyString())).thenReturn(role);

        invocationServicesUtil.changeRolesByDepartmentService(eventQueue, newHead, oldHead, parentEventId, author);

        verify(roleCommandService, times(2)).addToQueueUserRoleChangedEvents(isA(Queue.class), isA(User.class), isA(Role.class), isA(String.class), isA(User.class));
    }
}