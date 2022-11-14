package com.rm.toolkit.user.query.service;

import com.rm.toolkit.user.query.dto.query.UserQuery;
import com.rm.toolkit.user.query.exception.notfound.UserNotFoundException;
import com.rm.toolkit.user.query.model.User;
import com.rm.toolkit.user.query.model.UserMediumInfo;
import com.rm.toolkit.user.query.model.UserMinimalInfo;
import com.rm.toolkit.user.query.repository.CustomUserRepository;
import com.rm.toolkit.user.query.repository.UserRepository;
import com.rm.toolkit.user.query.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryService {

    private final UserRepository userRepository;
    private final CustomUserRepository customUserRepository;
    private final ValidationUtil validationUtil;

    @Transactional(readOnly = true)
    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден", userId);
                    throw new UserNotFoundException(userId);
                });
    }

    @Transactional(readOnly = true)
    public Page<UserMediumInfo> getUsers(int page, int size, UserQuery query) {
        Pageable pageable = PageRequest.of(page, size);
        return customUserRepository.findAllByUserQuery(query, pageable);
    }


    @Transactional(readOnly = true)
    public Page<UserMinimalInfo> getUsersMinimal(int page, int size, UserQuery query) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserMediumInfo> userMediumInfos = customUserRepository.findAllByUserQuery(query, pageable);
        List<UserMinimalInfo> userMinimalInfos = userMediumInfos.toList().stream().map(UserMinimalInfo::new)
                .collect(Collectors.toList());
        return new PageImpl<>(userMinimalInfos, pageable, userMinimalInfos.size());
    }

    @Transactional(readOnly = true)
    public Set<UserMinimalInfo> getUsersMinimalInfoByDepartment(String departmentId) {
        return userRepository.findAllByDepartmentIdMinimalInfo(departmentId);
    }

    @Transactional(readOnly = true)
    public boolean isUserInPool(String userId, String authorId) {
        User user = validationUtil.findUserIfExists(userId);
        String rmId = user.getResourceManager().getId();
        while (rmId != null) {
            if (authorId.equals(rmId))
                return true;
            User nextRm = validationUtil.findUserIfExists(rmId);
            rmId = nextRm.getResourceManager().getId();
        }
        return false;
    }
}
