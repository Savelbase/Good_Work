package com.rm.toolkit.user.query.repository.impl;

import com.rm.toolkit.user.query.dto.query.UserQuery;
import com.rm.toolkit.user.query.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.model.UserBase;
import com.rm.toolkit.user.query.model.UserMediumInfo;
import com.rm.toolkit.user.query.model.embedded.ActivityEmbedded;
import com.rm.toolkit.user.query.model.embedded.DepartmentEmbedded;
import com.rm.toolkit.user.query.model.embedded.RoleEmbedded;
import com.rm.toolkit.user.query.model.embedded.UserEmbedded;
import com.rm.toolkit.user.query.model.type.StatusType;
import com.rm.toolkit.user.query.repository.CustomUserRepository;
import com.rm.toolkit.user.query.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private static final int firstName = 0;
    private static final int lastName = 1;
    private static final int ONE_WORD = 1;
    private static final int TWO_WORD = 2;

    @Override
    @Transactional(readOnly = true)
    public Page<UserMediumInfo> findAllByUserQuery(UserQuery userQuery, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserMediumInfo> cq = cb.createQuery(UserMediumInfo.class);
        Root<UserMediumInfo> userRoot = cq.from(UserMediumInfo.class);

        List<Predicate> predicates = new ArrayList<>();

        fillPredicates(predicates, userQuery, userRoot, cb);

        cq.select(userRoot).where(predicates.toArray(new Predicate[]{}));

        TypedQuery<UserMediumInfo> query = entityManager.createQuery(cq);

        List<UserMediumInfo> usersByCriteria = query.getResultList();

        if (userQuery.getFullName() != null) {
            List<String> name = Arrays.asList(userQuery.getFullName().trim().split("\\s"));
            if (name.size() == ONE_WORD) {
                List<UserMediumInfo> usersByCriteriaNameAndFirstName = new ArrayList<>(findUserByFirstName(name.get(firstName), name.get(firstName), pageable));
                return getUserMediumInfos(userQuery, pageable, usersByCriteria, usersByCriteriaNameAndFirstName);
            } else if (name.size() >= TWO_WORD) {
                List<UserMediumInfo> usersByCriteriaNameAndFirstName = new ArrayList<>(findUserByLastNameAndFirstName(name.get(firstName), name.get(lastName), pageable));
                return getUserMediumInfos(userQuery, pageable, usersByCriteria, usersByCriteriaNameAndFirstName);
            }
        }

        return getUserActivity(userQuery, pageable, usersByCriteria);
    }

    private Page<UserMediumInfo> getUserMediumInfos(UserQuery userQuery, Pageable pageable, List<UserMediumInfo> usersByCriteria, List<UserMediumInfo> usersByCriteriaNameAndFirstName) {
        List<UserMediumInfo> selectUsersAllFilters = new ArrayList<>();

        for (UserMediumInfo usersByCriterion : usersByCriteria) {
            for (UserMediumInfo userMediumInfo : usersByCriteriaNameAndFirstName) {
                if (usersByCriterion.getId().equals(userMediumInfo.getId())) {
                    selectUsersAllFilters.add(usersByCriterion);
                }
            }
        }
        return getUserActivity(userQuery, pageable, selectUsersAllFilters);
    }

    private Page<UserMediumInfo> getUserActivity(UserQuery userQuery, Pageable pageable, List<UserMediumInfo> usersByCriteria) {
        if (userQuery.getActivityId() != null) {
            usersByCriteria = usersByCriteria.stream().filter(u -> {
                if (u.getActivities() == null && !userQuery.getActivityId().isEmpty()) return false;
                return u.getActivities().stream().map(ActivityEmbedded::getId).collect(Collectors.toSet())
                        .containsAll(userQuery.getActivityId());
            }).collect(Collectors.toList());
        }
        if (userQuery.isSortByName()) {
            usersByCriteria.sort(Comparator.comparing(UserBase::getFirstName).thenComparing(UserBase::getLastName));
        } else {
            usersByCriteria.sort(Comparator.comparing(UserBase::getLastName).thenComparing(UserBase::getFirstName));
        }

        return new PageImpl<>(usersByCriteria.stream().skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize()).collect(Collectors.toList()), pageable, usersByCriteria.size());
    }

    @Override
    public List<UserMediumInfo> findUserByLastNameAndFirstName(String firstName, String lastName, Pageable pageable) {
        return userRepository.findUserByLastNameAndFirstName(firstName, lastName, pageable);
    }

    @Override
    public List<UserMediumInfo> findUserByFirstName(String firstName, String lastName, Pageable pageable) {
        return userRepository.findUserByFirstName(firstName, lastName, pageable);
    }

    @Transactional(readOnly = true)
    public Set<User> getSubordinates(User rm, boolean rmsOnly) {
        Set<User> subordinates = new HashSet<>();

        Queue<User> subordinatesQueue = new ArrayDeque<>(userRepository.findAllByResourceManagerId(rm.getId()));
        Queue<User> subSubordinatesQueue = new ArrayDeque<>();
        while (!subordinatesQueue.isEmpty()) {
            for (User user : subordinatesQueue) {
                if (!rmsOnly || user.isRm()) {
                    subordinates.add(user);
                    subSubordinatesQueue.addAll(userRepository.findAllByResourceManagerId(user.getId()));
                }
            }
            subordinatesQueue = new ArrayDeque<>(subSubordinatesQueue);
            subSubordinatesQueue.clear();
        }

        return subordinates;
    }

    @Transactional(readOnly = true)
    public void fillPredicates(List<Predicate> predicates, UserQuery userQuery,
                               Root<UserMediumInfo> userRoot, CriteriaBuilder cb) {
        Set<String> rolesSet = new HashSet<>();

        if (userQuery.getResourceManagerId() != null) {
            if (userQuery.isRecursive()) {
                User resourceManager = userRepository.findById(userQuery.getResourceManagerId()).orElseThrow(() -> {
                    log.info("RM с id {} не существует", userQuery.getResourceManagerId());
                    throw new UserNotFoundException(userQuery.getResourceManagerId());
                });

                predicates.add(
                        userRoot.get("id").in(getSubordinates(resourceManager, userQuery.isRmsOnly())
                                .stream().map(UserBase::getId).collect(Collectors.toList()))
                );
            } else {
                predicates.add(
                        cb.equal(userRoot.<UserEmbedded>get("resourceManager").<String>get("id"), userQuery.getResourceManagerId())
                );
            }
        }

        if (userQuery.getDepartmentId() != null) {
            predicates.add(
                    userRoot.<DepartmentEmbedded>get("department").<String>get("id").in(userQuery.getDepartmentId())
            );
        }

        if (userQuery.getRoleId() != null) {
            predicates.add(
                    userRoot.<RoleEmbedded>get("role").<String>get("id").in(userQuery.getRoleId())
            );
        }

        if (userQuery.isRmsOnly()) {
            predicates.add(
                    cb.equal(userRoot.<Boolean>get("isRm"), userQuery.isRmsOnly())
            );
        }

        fillRolesSet(rolesSet, userQuery);

        if (!rolesSet.isEmpty()) {
            predicates.add(
                    userRoot.<RoleEmbedded>get("role").<String>get("name").in(rolesSet)
            );
        }

        if (userQuery.getStatus() != null) {
            predicates.add(
                    cb.equal(userRoot.<StatusType>get("status"), userQuery.getStatus())
            );
        } else {
            predicates.add((
                    cb.notEqual(userRoot.<StatusType>get("status"), StatusType.DELETED)
            ));
        }
    }

    @Transactional(readOnly = true)
    public void fillRolesSet(Set<String> rolesSet, UserQuery userQuery) {
        if (userQuery.isShowEmployee()) {
            rolesSet.add("Employee");
        }

        if (userQuery.isShowArm()) {
            rolesSet.add("ARM");
        }

        if (userQuery.isShowRm()) {
            rolesSet.add("RM");
        }

        if (userQuery.isShowSrm()) {
            rolesSet.add("SRM");
        }

        if (userQuery.isShowRd()) {
            rolesSet.add("RD");
        }

        if (userQuery.isShowAdmin()) {
            rolesSet.add("Admin");
        }
    }
}
