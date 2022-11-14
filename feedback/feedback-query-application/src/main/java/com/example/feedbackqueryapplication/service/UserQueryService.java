package com.example.feedbackqueryapplication.service;

import com.example.feedbackqueryapplication.exception.notfound.UserNotFoundException;
import com.example.feedbackqueryapplication.model.User;
import com.example.feedbackqueryapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean isUserInPoolOfSubordinates(int managerRolePriority, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователь с id {} не найден", userId);
            throw new UserNotFoundException(userId);
        });

        return managerRolePriority > user.getRole().getRolePriority();
    }
}
