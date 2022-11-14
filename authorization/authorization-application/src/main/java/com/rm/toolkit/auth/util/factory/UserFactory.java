package com.rm.toolkit.auth.util.factory;

import com.rm.toolkit.auth.model.User;
import com.rm.toolkit.auth.model.type.StatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public User createUser(String email, String password, String roleId) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .email(email.toLowerCase())
                .password(passwordEncoder.encode(password))
                .roleId(roleId)
                .loginAttempts(0)
                .version(1)
                .status(StatusType.ACTIVE)
                .build();
    }
}
