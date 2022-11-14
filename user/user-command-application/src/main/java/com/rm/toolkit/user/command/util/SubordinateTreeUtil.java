package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.event.Event;
import com.rm.toolkit.user.command.event.EventPayload;
import com.rm.toolkit.user.command.event.user.UserRmChangedEvent;
import com.rm.toolkit.user.command.message.projector.UserProjector;
import com.rm.toolkit.user.command.model.Activity;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.model.User;
import com.rm.toolkit.user.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubordinateTreeUtil {

    private final UserRepository userRepository;
    private final UserProjector userProjector;
    private final ValidationUtil validationUtil;
    private final EventUtil eventUtil;

    public Map<String, String> activitiesToMap(Set<Activity> activities) {
        return activities.stream().collect(Collectors.toMap(Activity::getId, Activity::getName));
    }

    @Transactional(readOnly = true)
    public Collection<User> getDirectSubordinates(User rm, Collection<User> currentlyRemovedUsers,
                                                  Map<User, User> changedRms) {
        return getDirectSubordinates(rm, currentlyRemovedUsers, changedRms, 1, 10);
    }

    @Transactional(readOnly = true)
    public Collection<User> getDirectSubordinates(User rm, Collection<User> currentlyRemovedUsers,
                                                  Map<User, User> changedRms, Integer minRolePriority,
                                                  Integer maxRolePriority) {
        Set<User> subordinates = userRepository.findAllByResourceManagerId(rm.getId());
        subordinates.removeAll(currentlyRemovedUsers);
        for (Map.Entry<User, User> changedRmsPair : changedRms.entrySet()) {
            if (changedRmsPair.getValue().getId().equals(rm.getId())) {
                subordinates.add(changedRmsPair.getKey());
            }
        }

        return subordinates;
    }

    @Transactional(readOnly = true)
    public Collection<User> getAllSubordinates(User rm) {
        Set<User> subordinates = new HashSet<>();

        Queue<User> queue = new ArrayDeque<>(userRepository.findAllByResourceManagerId(rm.getId()));
        Queue<User> queue2 = new ArrayDeque<>();
        while (!queue.isEmpty()) {
            for (User user : queue) {
                subordinates.add(user);
                queue2.addAll(userRepository.findAllByResourceManagerId(user.getId()));
            }
            queue = new ArrayDeque<>(queue2);
            queue2.clear();
        }

        return subordinates;
    }

    @Transactional(readOnly = true)
    public Map<String, User> getAllRmSubordinates(User rm) {
        Map<String, User> subordinates = new HashMap<>();

        Queue<User> queue = new ArrayDeque<>(userRepository.findAllByResourceManagerId(rm.getId()));
        Queue<User> queue2 = new ArrayDeque<>();
        while (!queue.isEmpty()) {
            for (User user : queue) {
                Role role = validationUtil.findRoleIfExist(user.getRoleId());
                if (validationUtil.isRm(role)) {
                    subordinates.put(user.getId(), user);
                    queue2.addAll(userRepository.findAllByResourceManagerId(user.getId()));
                }
            }
            queue = new ArrayDeque<>(queue2);
            queue2.clear();
        }

        return subordinates;
    }

    @Transactional(readOnly = true)
    public void addToQueueEventsToMoveUsersUp(Queue<Event<? extends EventPayload>> eventQueue,
                                              Collection<User> removedUsersFromDepartment, String authorId,
                                              String parentEventId) {
        Map<User, User> changedRms = returnWhatHappensWhenMoveUsersUp(removedUsersFromDepartment);
        addToQueueEventsToMoveUsersUp(eventQueue, changedRms, authorId, parentEventId);
    }

    /**
     * @param removedUsersFromDepartment - кто удаляется из отдела
     * @return Map<Бывший прямой подчинённый удалённого из отдела пользователя, его новый RM>
     */
    @Transactional(readOnly = true)
    public Map<User, User> returnWhatHappensWhenMoveUsersUp(Collection<User> removedUsersFromDepartment) {
        // <user, newResourceManager>
        Map<User, User> changedRms = new HashMap<>();
        Set<User> currentlyRemovedUsers = new HashSet<>();

        for (User removedUser : removedUsersFromDepartment) {
            currentlyRemovedUsers.add(removedUser);

            for (User subordinate : getDirectSubordinates(removedUser, currentlyRemovedUsers, changedRms)) {
                User newRm;
                if (changedRms.containsKey(removedUser)) {
                    newRm = changedRms.get(removedUser);
                } else {
                    newRm = validationUtil.findUserIfExist(removedUser.getResourceManagerId());
                }
                changedRms.put(subordinate, newRm);
            }
        }

        return changedRms;
    }

    public void addToQueueEventsToMoveUsersUp(Queue<Event<? extends EventPayload>> eventQueue, Map<User, User> changedRms,
                                              String authorId, String parentEventId) {
        for (Map.Entry<User, User> entry : changedRms.entrySet()) {
            User user = entry.getKey();
            UserRmChangedEvent event = generateUserRmChangedEvent(user, entry.getValue(), authorId,
                    parentEventId);
            eventQueue.add(event);
            userProjector.project(event, user);
        }
    }

    public UserRmChangedEvent generateUserRmChangedEvent(User user, User newRm, String authorId, String parentEventId) {
        UserRmChangedEvent.Payload payload = new UserRmChangedEvent.Payload(newRm.getId(), newRm.getFirstName(),
                newRm.getLastName());
        UserRmChangedEvent userRmChangedEvent = new UserRmChangedEvent();
        eventUtil.populateEventFields(userRmChangedEvent, user.getId(), user.getVersion() + 1, authorId, payload,
                parentEventId);
        return userRmChangedEvent;
    }
}
