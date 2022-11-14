package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.service.RoleCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Queue;

@Component
@Slf4j
@RequiredArgsConstructor
public class InvocationServicesUtil {

    private final RoleCommandService roleCommandService;
    private final ValidationUtil validationUtil;

    private static final String EMPLOYEE_ROLE = "579fc993-6123-419a-ae3c-96b0b230f834";
    private static final String RD_ROLE = "43b768e7-c117-4da6-9f41-9b6497aa7b31";

    /**
     * Установка новому главе роль RD, а старому, если он есть, Employee
     *
     * @param eventQueue    Очередь событий
     * @param newHead       новый глава отдела
     * @param oldHead       старый глава отдела, null для нового отдела
     * @param parentEventId событие, запустившее очередь
     * @param author        автор события
     */
    @Transactional
    public void changeRolesByDepartmentService(Queue<Event<? extends EventPayload>> eventQueue,
                                               User newHead, User oldHead, String parentEventId, User author) {
        if (!Objects.isNull(oldHead)) {
            Role employee = validationUtil.findRoleIfExist(EMPLOYEE_ROLE);
            roleCommandService.addToQueueUserRoleChangedEvents(eventQueue, oldHead, employee, parentEventId, author);
        }
        Role rd = validationUtil.findRoleIfExist(RD_ROLE);
        roleCommandService.addToQueueUserRoleChangedEvents(eventQueue, newHead, rd, parentEventId, author);
    }

    @Transactional
    public void changeRolesByDepartmentService(Queue<Event<? extends EventPayload>> eventQueue,
                                               User newHead, String parentEventId, User author) {
        changeRolesByDepartmentService(eventQueue, newHead, null, parentEventId, author);
    }
}
