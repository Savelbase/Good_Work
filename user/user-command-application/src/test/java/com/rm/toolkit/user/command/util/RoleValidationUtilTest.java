package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.exception.conflict.RoleAlreadyExistsException;
import com.rm.toolkit.user.command.exception.conflict.UserCantBecomeDepartmentHeadException;
import com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.repository.RoleRepository;
import com.rm.toolkit.user.command.security.AuthorityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RoleValidationUtil.class})
@ExtendWith(SpringExtension.class)
class RoleValidationUtilTest {
    @Autowired
    private RoleValidationUtil roleValidationUtil;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    void canBecomeDepartmentHead() {
        Role arm = new Role();
        arm.setName("ARM");
        arm.setPriority(3);
        Role rm = new Role();
        rm.setName("RM");
        rm.setPriority(5);
        Role srm = new Role();
        srm.setName("SRM");
        srm.setPriority(7);
        Role rd = new Role();
        rd.setName("RD");
        rd.setPriority(9);
        Role invalidRole = new Role();
        invalidRole.setName("Test");
        invalidRole.setPriority(0);

        assertTrue(roleValidationUtil.canBecomeDepartmentHead(arm));
        assertTrue(roleValidationUtil.canBecomeDepartmentHead(rm));
        assertTrue(roleValidationUtil.canBecomeDepartmentHead(srm));
        assertTrue(roleValidationUtil.canBecomeDepartmentHead(rd));
        assertFalse(roleValidationUtil.canBecomeDepartmentHead(invalidRole));
    }

    @Test
    void isRm() {
        Role rm = new Role();
        AuthorityType[] authorities = new AuthorityType[]{AuthorityType.ONE_TO_ONE};
        rm.setAuthorities(authorities);

        assertTrue(roleValidationUtil.isRm(rm));
    }

    @Test
    void isAdminByRole() {
        AuthorityType[] authorities = new AuthorityType[]{AuthorityType.EDIT_ROLES,
                AuthorityType.EDIT_DEPARTMENTS, AuthorityType.EDIT_INTERVALS};
        Role admin = new Role();
        admin.setAuthorities(authorities);

        assertTrue(roleValidationUtil.isAdmin(admin));
    }

    @Test
    void isAdminByAuthorities() {
        Set<AuthorityType> authorities = new HashSet<>();
        authorities.add(AuthorityType.EDIT_ROLES);
        authorities.add(AuthorityType.EDIT_DEPARTMENTS);
        authorities.add(AuthorityType.EDIT_INTERVALS);

        assertTrue(roleValidationUtil.isAdmin(authorities));
    }

    @Test
    void checkIfOtherRolePriorityHigherOrEqualsThanAuthors() {
        String roleId = "Test";
        String authorId = "Test";
        int rolePriority = 1;
        Role role = new Role();
        role.setPriority(rolePriority);
        int authorPriorityHigher = 2;
        int authorPriorityEqual = 1;
        int authorPriorityLower = 0;

        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> roleValidationUtil.checkIfOtherRolePriorityHigherOrEqualsThanAuthors(roleId, authorPriorityHigher, authorId));
        assertThrows(AccessDeniedException.class, () -> roleValidationUtil.checkIfOtherRolePriorityHigherOrEqualsThanAuthors(roleId, authorPriorityEqual, authorId));
        assertThrows(AccessDeniedException.class, () -> roleValidationUtil.checkIfOtherRolePriorityHigherOrEqualsThanAuthors(roleId, authorPriorityLower, authorId));
    }

    @Test
    void checkIfOtherRolePriorityHigherThanAuthors() {
        String roleId = "Test";
        String authorId = "Test";
        int rolePriority = 1;
        Role role = new Role();
        role.setPriority(rolePriority);
        int authorPriorityHigher = 2;
        int authorPriorityEqual = 1;
        int authorPriorityLower = 0;

        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> roleValidationUtil.checkIfOtherRolePriorityHigherThanAuthors(roleId, authorPriorityHigher, authorId));
        assertDoesNotThrow(() -> roleValidationUtil.checkIfOtherRolePriorityHigherThanAuthors(roleId, authorPriorityEqual, authorId));
        assertThrows(AccessDeniedException.class, () -> roleValidationUtil.checkIfOtherRolePriorityHigherThanAuthors(roleId, authorPriorityLower, authorId));
    }

    @Test
    void checkIfRolePriorityWithinBounds() {
        int validPriority = 1;
        int lowerInvalidPriority = 0;
        int upperInvalidPriority = 10;

        assertDoesNotThrow(() -> roleValidationUtil.checkIfRolePriorityWithinBounds(validPriority));
        assertThrows(RolePriorityOutOfBoundsException.class, () -> roleValidationUtil.checkIfRolePriorityWithinBounds(lowerInvalidPriority));
        assertThrows(RolePriorityOutOfBoundsException.class, () -> roleValidationUtil.checkIfRolePriorityWithinBounds(upperInvalidPriority));
    }

    @Test
    void checkThatRoleDoesntExist() {
        String roleId = "Test";

        when(roleRepository.existsByNameIgnoreCase(roleId)).thenReturn(false);

        assertDoesNotThrow(() -> roleValidationUtil.checkThatRoleDoesntExist(roleId));
    }

    @Test
    void shouldThrowRoleAlreadyExistsExceptionOnCheckThatRoleDoesntExist() {
        String roleId = "Test";

        when(roleRepository.existsByNameIgnoreCase(roleId)).thenReturn(true);

        assertThrows(RoleAlreadyExistsException.class, () -> roleValidationUtil.checkThatRoleDoesntExist(roleId));
    }

    @Test
    void findRoleIfExist() {
        String roleId = "Test";
        Role role = new Role();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> roleValidationUtil.findRoleIfExist(roleId));
    }

    @Test
    void shouldThrowRoleNotFoundExceptionOnFindRoleIfExist() {
        String roleId = "Test";

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleValidationUtil.findRoleIfExist(roleId));
    }

    @Test
    void checkIfCanBecomeDepartmentHead() {
        Role arm = new Role();
        arm.setName("ARM");
        arm.setPriority(3);
        Role rm = new Role();
        rm.setName("RM");
        rm.setPriority(5);
        Role srm = new Role();
        srm.setName("SRM");
        srm.setPriority(7);
        Role rd = new Role();
        rd.setName("RD");
        rd.setPriority(9);
        Role invalidRole = new Role();
        invalidRole.setName("Test");
        invalidRole.setPriority(0);

        assertDoesNotThrow(() -> roleValidationUtil.checkIfCanBecomeDepartmentHead(arm));
        assertDoesNotThrow(() -> roleValidationUtil.checkIfCanBecomeDepartmentHead(rm));
        assertDoesNotThrow(() -> roleValidationUtil.checkIfCanBecomeDepartmentHead(srm));
        assertDoesNotThrow(() -> roleValidationUtil.checkIfCanBecomeDepartmentHead(rd));
        assertThrows(UserCantBecomeDepartmentHeadException.class, () -> roleValidationUtil.checkIfCanBecomeDepartmentHead(invalidRole));
    }

    @Test
    void isRmRole() {
        Role arm = new Role();
        arm.setName("ARM");
        arm.setPriority(3);
        Role rm = new Role();
        rm.setName("RM");
        rm.setPriority(5);
        Role srm = new Role();
        srm.setName("SRM");
        srm.setPriority(7);
        Role rd = new Role();
        rd.setName("RD");
        rd.setPriority(9);
        Role invalidRole = new Role();
        invalidRole.setName("Test");
        invalidRole.setPriority(0);

        assertTrue(roleValidationUtil.isRmRole(arm));
        assertTrue(roleValidationUtil.isRmRole(rm));
        assertTrue(roleValidationUtil.isRmRole(srm));
        assertTrue(roleValidationUtil.isRmRole(rd));
        assertFalse(roleValidationUtil.isRmRole(invalidRole));
    }
}