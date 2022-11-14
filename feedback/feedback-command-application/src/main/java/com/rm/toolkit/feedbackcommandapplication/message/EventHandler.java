package com.rm.toolkit.feedbackcommandapplication.message;

import com.rm.toolkit.feedbackcommandapplication.event.Event;
import com.rm.toolkit.feedbackcommandapplication.event.EventPayload;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.department.DepartmentEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCompletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneDeletedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.oneToOne.OneToOneUpdatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.ChangeUsersDepartmentEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserCreatedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserDepartmentChangedEvent;
import com.rm.toolkit.feedbackcommandapplication.event.user.UserEditedEvent;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.DepartmentNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.OneToOneNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.exception.notfound.UserNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.message.projector.DepartmentProjector;
import com.rm.toolkit.feedbackcommandapplication.message.projector.OneToOneProjector;
import com.rm.toolkit.feedbackcommandapplication.message.projector.UserProjector;
import com.rm.toolkit.feedbackcommandapplication.model.Department;
import com.rm.toolkit.feedbackcommandapplication.model.OneToOne;
import com.rm.toolkit.feedbackcommandapplication.model.User;
import com.rm.toolkit.feedbackcommandapplication.repository.DepartmentRepository;
import com.rm.toolkit.feedbackcommandapplication.repository.OneToOneRepository;
import com.rm.toolkit.feedbackcommandapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final UserRepository userRepository;
    private final UserProjector userProjector;
    private final OneToOneRepository oneToOneRepository;
    private final OneToOneProjector oneToOneProjector;
    private final DepartmentRepository departmentRepository;
    private final DepartmentProjector departmentProjector;

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
    public void handle(UserDepartmentChangedEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил отдел после получения события", user.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(ChangeUsersDepartmentEvent event) {
        User user = getUserFromEvent(event);
        userProjector.project(event, user);
        userRepository.save(user);

        log.info("Пользователь с id {} изменил департамент пользователей после получения события", user.getId());
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneCreatedEvent event) {
        OneToOne oneToOne = oneToOneProjector.project(event);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} создан после получения события", oneToOne.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneUpdatedEvent event) {
        OneToOne oneToOne = getOneToOneFromEvent(event);

        oneToOneProjector.project(event, oneToOne);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} отредактирован после получения события", oneToOne.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneDeletedEvent event) {
        OneToOne oneToOne = getOneToOneFromEvent(event);

        oneToOneProjector.project(event, oneToOne);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} удалён после получения события", oneToOne.getId());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void handle(OneToOneCompletedEvent event) {
        OneToOne oneToOne = getOneToOneFromEvent(event);

        oneToOneProjector.project(event, oneToOne);
        oneToOneRepository.save(oneToOne);

        log.info("One-to-one с id {} переведен в статус Завершенный после получения события", oneToOne.getId());
    }

    private User getUserFromEvent(Event<? extends EventPayload> event) {
        String userId = event.getEntityId();
        return userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь с id {} не существует", userId);
            throw new UserNotFoundException(userId);
        });
    }

    private Department getDepartmentFromEvent(Event<? extends EventPayload> event) {
        String departmentId = event.getEntityId();
        return departmentRepository.findById(departmentId).orElseThrow(() -> {
            log.info("Отдела с id {} не существует", departmentId);
            throw new DepartmentNotFoundException(departmentId);
        });
    }

    private OneToOne getOneToOneFromEvent(Event<? extends EventPayload> event) {
        String oneToOneId = event.getEntityId();
        return oneToOneRepository.findById(oneToOneId).orElseThrow(() -> {
            log.info("121 встречи с id {} не существует", oneToOneId);
            throw new OneToOneNotFoundException(oneToOneId);
        });
    }
}
