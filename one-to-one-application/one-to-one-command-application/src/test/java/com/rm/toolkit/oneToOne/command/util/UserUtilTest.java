package com.rm.toolkit.oneToOne.command.util;

import com.rm.toolkit.oneToOne.command.exception.UserInPoolIsAbsentException;
import com.rm.toolkit.oneToOne.command.exception.UserNotFoundException;
import com.rm.toolkit.oneToOne.command.model.User;
import com.rm.toolkit.oneToOne.command.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ContextConfiguration(classes = UserUtil.class)
@ExtendWith(SpringExtension.class)
public class UserUtilTest {

    @Autowired
    private UserUtil userUtil;

    @MockBean
    private UserRepository userRepository;

    User user = new User();
    User resourceManager = new User();
    User admin = new User();

    @BeforeEach
    void preparationOfData() {
        user.setId("userIdForTest");
        user.setResourceManagerId("resourceMangerIdForTest");
        resourceManager.setId(user.getResourceManagerId());
        resourceManager.setResourceManagerId("adminIdForTest");
        admin.setResourceManagerId(resourceManager.getId());
        admin.setResourceManagerId(null);
    }

    @Test
    void findUserIfExistsShouldUseRepository() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userUtil.findUserIfExists(user.getId());
        Mockito.verify(userRepository).findById(user.getId());
    }

    @Test
    void findUserIfExistShouldThrowException() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userUtil.findUserIfExists(user.getId()));
    }

    @Test
    void checkIfUserExistInPoolShouldUseRepository() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(resourceManager.getId())).thenReturn(Optional.of(resourceManager));
        userUtil.checkIfUserExistInPool(user.getId(), resourceManager.getId());
        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(userRepository).findById(resourceManager.getId());
    }

    @Test
    void checkIfUserExistInPoolShouldThrowException() {
        user.setResourceManagerId(admin.getId());
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(resourceManager.getId())).thenReturn(Optional.of(resourceManager));
        Mockito.when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        Assertions.assertThrows(UserInPoolIsAbsentException.class, () -> userUtil.checkIfUserExistInPool(user.getId(), resourceManager.getId()));
    }


}
