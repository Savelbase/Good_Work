package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.exception.badrequest.BadRequestException;
import com.rm.toolkit.user.command.exception.badrequest.FieldTooLongException;
import com.rm.toolkit.user.command.exception.conflict.DepartmentAlreadyExistsException;
import com.rm.toolkit.user.command.exception.conflict.UserAlreadyExistException;
import com.rm.toolkit.user.command.exception.conflict.UserHasDepartmentsException;
import com.rm.toolkit.user.command.exception.notfound.*;
import com.rm.toolkit.user.command.exception.unprocessableentity.OnlyAdminShouldHaveSettingsAuthorityException;
import com.rm.toolkit.user.command.model.*;
import com.rm.toolkit.user.command.model.type.StatusType;
import com.rm.toolkit.user.command.repository.*;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.rm.toolkit.user.command.testUtil.TestModelsBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ValidationUtil.class})
@ExtendWith(SpringExtension.class)
@MockBeans({@MockBean(UserRepository.class), @MockBean(CityRepository.class),
        @MockBean(CountryRepository.class), @MockBean(DepartmentRepository.class),
        @MockBean(SubordinateTreeUtil.class), @MockBean(ActivityRepository.class),
        @MockBean(RoleValidationUtil.class)})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ValidationUtilTest {
    private final ValidationUtil validationUtil;

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final DepartmentRepository departmentRepository;
    private final ActivityRepository activityRepository;
    private final RoleValidationUtil roleValidationUtil;

    private static final int MAX_EMAIL_LENGTH = 35;
    private static final int MAX_ROLE_NAME_LENGTH = 20;
    private static final int MAX_DEPARTMENT_NAME_LENGTH = 30;

    public static final List<AuthorityType> HEAD_AUTHORITIES = List.of(AuthorityType.USER_STATUS_SETTINGS,
            AuthorityType.ADD_EMPLOYEE_TO_DEPARTMENT);

    @Test
    void isRm() {
        Role role = new Role();

        when(roleValidationUtil.isRm(any(Role.class))).thenReturn(true);

        assertTrue(validationUtil.isRm(role));
        verify(roleValidationUtil).isRm(isA(Role.class));
    }

    @Test
    void isAdminByRole() {
        Role role = new Role();

        when(roleValidationUtil.isAdmin(any(Role.class))).thenReturn(true);

        assertTrue(validationUtil.isAdmin(role));
        verify(roleValidationUtil).isAdmin(isA(Role.class));
    }

    @Test
    void isAdminByAuthorities() {
        List<AuthorityType> authorities = new ArrayList<>();

        when(roleValidationUtil.isAdmin(authorities)).thenReturn(true);

        assertTrue(validationUtil.isAdmin(authorities));
        verify(roleValidationUtil).isAdmin(authorities);
    }

    @Test
    void findUserIfExist() {
        String userId = "Test";
        User user = new User();

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> validationUtil.findUserIfExist(userId));
        verify(userRepository).findById(isA(String.class));
    }

    @Test
    void shouldThrowUserNotFoundExceptionOnFindUserIfExist() {
        String userId = "Test";

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> validationUtil.findUserIfExist(userId));
    }

    @Test
    void findCityIfExist() {
        String cityId = "Test";
        City city = new City();

        when(cityRepository.findById(anyString())).thenReturn(Optional.of(city));

        assertDoesNotThrow(() -> validationUtil.findCityIfExist(cityId));
        verify(cityRepository).findById(isA(String.class));
    }

    @Test
    void shouldThrowCityNotFoundExceptionOnFindCityIfExist() {
        String cityId = "Test";

        when(cityRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> validationUtil.findCityIfExist(cityId));
        verify(cityRepository).findById(isA(String.class));
    }

    @Test
    void findCountryIfExist() {
        String countryId = "Test";
        Country country = new Country();

        when(countryRepository.findById(anyString())).thenReturn(Optional.of(country));

        assertDoesNotThrow(() -> validationUtil.findCountryIfExist(countryId));
        verify(countryRepository).findById(isA(String.class));
    }

    @Test
    void shouldThrowCountryNotFoundExceptionOnFindCountryIfExist() {
        String countryId = "Test";

        when(countryRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> validationUtil.findCountryIfExist(countryId));
        verify(countryRepository).findById(isA(String.class));
    }

    @Test
    void findDepartmentIfExist() {
        String departmentId = "Test";
        Department department = new Department();

        when(departmentRepository.findById(anyString())).thenReturn(Optional.of(department));

        assertDoesNotThrow(() -> validationUtil.findDepartmentIfExist(departmentId));
        verify(departmentRepository).findById(isA(String.class));
    }

    @Test
    void shouldThrowDepartmentNotFoundExceptionOnFindDepartmentIfExist() {
        String departmentId = "Test";

        when(departmentRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> validationUtil.findDepartmentIfExist(departmentId));
        verify(departmentRepository).findById(isA(String.class));
    }

    @Test
    void findRoleIfExist() {
        String roleId = "Test";
        Role role = new Role();

        when(roleValidationUtil.findRoleIfExist(roleId)).thenReturn(role);

        assertEquals(role, validationUtil.findRoleIfExist(roleId));
        verify(roleValidationUtil).findRoleIfExist(isA(String.class));
    }

    @Test
    void findUserActivities() {
        String activityId = "Test";
        Set<String> activityIds = new HashSet<>();
        activityIds.add(activityId);
        Activity activity = new Activity();

        when(activityRepository.findById(anyString())).thenReturn(Optional.of(activity));

        Set<Activity> activities = validationUtil.findUserActivities(activityIds);

        assertFalse(activities.isEmpty());
        verify(activityRepository).findById(isA(String.class));
    }

    @Test
    void shouldThrowActivityNotFoundExceptionOnFindUserActivities() {
        String activityId = "Test";
        Set<String> activityIds = new HashSet<>();
        activityIds.add(activityId);

        when(activityRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class, () -> validationUtil.findUserActivities(activityIds));
        verify(activityRepository).findById(isA(String.class));
    }

    @Test
    void validateAuthorities() {
        Set<AuthorityType> authorities = new HashSet<>();

        when(roleValidationUtil.isAdmin(authorities)).thenReturn(false).thenReturn(true);

        assertDoesNotThrow(() -> validationUtil.validateAuthorities(authorities));
        assertThrows(OnlyAdminShouldHaveSettingsAuthorityException.class, () -> validationUtil.validateAuthorities(authorities));
    }

    @Test
    void checkThatUserDoesntExist() {
        String email = "Test";

        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false).thenReturn(true);

        assertDoesNotThrow(() -> validationUtil.checkThatUserDoesntExist(email));
        assertThrows(UserAlreadyExistException.class, () -> validationUtil.checkThatUserDoesntExist(email));
    }

    @Test
    void checkThatDepartmentDoesntExist() {
        String name = "Test";

        when(departmentRepository.existsByNameIgnoreCase(anyString())).thenReturn(false).thenReturn(true);

        assertDoesNotThrow(() -> validationUtil.checkThatDepartmentDoesntExist(name));
        assertThrows(DepartmentAlreadyExistsException.class, () -> validationUtil.checkThatDepartmentDoesntExist(name));
    }

    @Test
    void checkThatRoleDoesntExist() {
        String name = "Test";

        doNothing().when(roleValidationUtil).checkThatRoleDoesntExist(anyString());

        assertDoesNotThrow(() -> validationUtil.checkThatRoleDoesntExist(name));
        verify(roleValidationUtil).checkThatRoleDoesntExist(isA(String.class));
    }

    @Test
    void checkStatusForNull() {
        StatusType validStatus = StatusType.ACTIVE;

        assertDoesNotThrow(() -> validationUtil.checkStatusForNull(validStatus));
        assertThrows(BadRequestException.class, () -> validationUtil.checkStatusForNull(null));
    }

    @Test
    void checkIfAllowedToCreateUserWithThisRole() {
        int basePriority = 1;
        int validPriority = 2;
        int invalidPriority = 1;
        int lowPriority = 0;

        assertDoesNotThrow(() -> validationUtil.checkIfAllowedToCreateUserWithThisRole(basePriority, validPriority));
        assertThrows(AccessDeniedException.class, () -> validationUtil.checkIfAllowedToCreateUserWithThisRole(basePriority, invalidPriority));
        assertThrows(AccessDeniedException.class, () -> validationUtil.checkIfAllowedToCreateUserWithThisRole(basePriority, lowPriority));
    }

    @Test
    void checkIfRolePriorityWithinBounds() {
        int priority = 0;

        assertDoesNotThrow(() -> validationUtil.checkIfRolePriorityWithinBounds(priority));
        verify(roleValidationUtil).checkIfRolePriorityWithinBounds(isA(Integer.class));
    }

    @Test
    void checkPermissionsForManipulatingUser() {
        String authorId = "Test", roleId = "Test", departmentId = "Test";
        int authorRolePriority = 1;
        Set<AuthorityType> authorAuthorities = new HashSet<>();
        authorAuthorities.add(AuthorityType.EMPLOYEE_CARD);
        User user = new User();
        Role role = new Role();
        role.setPriority(0);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(roleValidationUtil.isAdmin(authorAuthorities)).thenReturn(true);
        when(roleValidationUtil.findRoleIfExist(anyString())).thenReturn(role);

        assertDoesNotThrow(() -> validationUtil.checkPermissionsForManipulatingUser(authorId, authorAuthorities, authorRolePriority, roleId, departmentId));
        verify(userRepository).findById(isA(String.class));
        verify(roleValidationUtil).isAdmin(authorAuthorities);
        verify(roleValidationUtil).findRoleIfExist(isA(String.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionOnCheckPermissionsForManipulatingUser() {
        String authorId = "Test", roleId = "Test", departmentId = "Test";
        int authorRolePriority = 1;
        Set<AuthorityType> invalidAuthorities = new HashSet<>();
        User user = new User();

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class, () -> validationUtil.checkPermissionsForManipulatingUser(authorId,
                invalidAuthorities, authorRolePriority, roleId, departmentId));
        verify(userRepository).findById(isA(String.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionOnCheckPermissionsForManipulatingUser2() {
        String authorId = "Test", roleId = "Test";
        String departmentId = "Valid department";
        int authorRolePriority = 1;
        Set<AuthorityType> validAuthorAuthorities = new HashSet<>();
        validAuthorAuthorities.add(AuthorityType.EMPLOYEE_CARD);
        User invalidHead = new User();
        invalidHead.setDepartmentId("Invalid department");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(invalidHead));
        when(roleValidationUtil.isAdmin(validAuthorAuthorities)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> validationUtil.checkPermissionsForManipulatingUser(authorId,
                validAuthorAuthorities, authorRolePriority, roleId, departmentId));
        verify(userRepository).findById(isA(String.class));
        verify(roleValidationUtil).isAdmin(validAuthorAuthorities);
    }

    @Test
    void shouldThrowAccessDeniedExceptionOnCheckPermissionsForManipulatingUser3() {
        String authorId = "Test", roleId = "Test";
        String departmentId = "Valid department";
        int authorRolePriority = 1;
        Set<AuthorityType> validAuthorAuthorities = new HashSet<>();
        validAuthorAuthorities.add(AuthorityType.EMPLOYEE_CARD);
        User validHead = new User();
        validHead.setDepartmentId(departmentId);
        Role roleWithHigherOrEqualPriority = new Role();
        roleWithHigherOrEqualPriority.setPriority(1);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(validHead));
        when(roleValidationUtil.isAdmin(validAuthorAuthorities)).thenReturn(false);
        when(roleValidationUtil.findRoleIfExist(anyString())).thenReturn(roleWithHigherOrEqualPriority);

        assertThrows(AccessDeniedException.class, () -> validationUtil.checkPermissionsForManipulatingUser(authorId,
                validAuthorAuthorities, authorRolePriority, roleId, departmentId));
        verify(userRepository).findById(isA(String.class));
        verify(roleValidationUtil).isAdmin(validAuthorAuthorities);
        verify(roleValidationUtil).findRoleIfExist(isA(String.class));
    }

    @Test
    void checkIfCanBecomeDepartmentHead() {
        Role role = new Role();
        doNothing().when(roleValidationUtil).checkIfCanBecomeDepartmentHead(any(Role.class));

        validationUtil.checkIfCanBecomeDepartmentHead(role);

        assertDoesNotThrow(() -> validationUtil.checkIfCanBecomeDepartmentHead(isA(Role.class)));
        verify(roleValidationUtil).checkIfCanBecomeDepartmentHead(isA(Role.class));
    }

    @Test
    void checkIfCanBecomeNewDepartmentHead() {
        Role role = new Role();
        User user = new User();
        user.setId("Test");
        user.setRoleId("Test");

        when(departmentRepository.findAllByHeadId(anyString())).thenReturn(new HashSet<>());
        when(roleValidationUtil.findRoleIfExist(anyString())).thenReturn(role);
        doNothing().when(roleValidationUtil).checkIfCanBecomeDepartmentHead(any(Role.class));

        assertDoesNotThrow(() -> validationUtil.checkIfCanBecomeNewDepartmentHead(user));
        verify(departmentRepository).findAllByHeadId(isA(String.class));
        verify(roleValidationUtil).findRoleIfExist(isA(String.class));
        verify(roleValidationUtil).checkIfCanBecomeDepartmentHead(isA(Role.class));
    }

    @Test
    void shouldThrowUserHasDepartmentsExceptionOnCheckIfCanBecomeNewDepartmentHead() {
        User user = new User();
        user.setId("Test");
        HashSet<Department> departmentsWithCurrentHead = new HashSet<>();
        departmentsWithCurrentHead.add(new Department());

        when(departmentRepository.findAllByHeadId(anyString())).thenReturn(departmentsWithCurrentHead);

        assertThrows(UserHasDepartmentsException.class, () -> validationUtil.checkIfCanBecomeNewDepartmentHead(user));
        verify(departmentRepository).findAllByHeadId(isA(String.class));
    }

    @Test
    void userShouldNotBeDepartmentHead() {
        User user = new User();
        user.setId("Test");

        when(departmentRepository.findAllByHeadId(anyString())).thenReturn(new HashSet<>());

        assertDoesNotThrow(() -> validationUtil.userShouldNotBeDepartmentHead(user));
        verify(departmentRepository).findAllByHeadId(isA(String.class));
    }

    @Test
    void shouldThrowUserHasDepartmentsExceptionOnUserShouldNotBeDepartmentHead() {
        User user = new User();
        user.setId("Test");
        HashSet<Department> departmentsWithCurrentHead = new HashSet<>();
        departmentsWithCurrentHead.add(new Department());

        when(departmentRepository.findAllByHeadId(anyString())).thenReturn(departmentsWithCurrentHead);

        assertThrows(UserHasDepartmentsException.class, () -> validationUtil.userShouldNotBeDepartmentHead(user));
        verify(departmentRepository).findAllByHeadId(isA(String.class));
    }

    @Test
    void checkIfOtherRolePriorityHigherThanAuthors() {
        String roleId = "Test", authorId = "Test";
        Integer authorRolePriority = 0;

        doNothing().when(roleValidationUtil).checkIfOtherRolePriorityHigherThanAuthors(anyString(), anyInt(), anyString());

        assertDoesNotThrow(() -> validationUtil.checkIfOtherRolePriorityHigherThanAuthors(roleId, authorRolePriority, authorId));
        verify(roleValidationUtil).checkIfOtherRolePriorityHigherThanAuthors(isA(String.class), isA(Integer.class), isA(String.class));
    }

    @Test
    void checkIfOtherRolePriorityHigherOrEqualsThanAuthors() {
        String roleId = "Test", authorId = "Test";
        Integer authorRolePriority = 0;

        doNothing().when(roleValidationUtil).checkIfOtherRolePriorityHigherOrEqualsThanAuthors(anyString(), anyInt(), anyString());

        assertDoesNotThrow(() -> validationUtil.checkIfOtherRolePriorityHigherOrEqualsThanAuthors(roleId, authorRolePriority, authorId));
        verify(roleValidationUtil).checkIfOtherRolePriorityHigherOrEqualsThanAuthors(isA(String.class), isA(Integer.class), isA(String.class));
    }

    @Test
    void checkIfEmailWithinLimit() {
        String email = RandomStringUtils.random(MAX_EMAIL_LENGTH);

        assertDoesNotThrow(() -> validationUtil.checkIfEmailWithinLimit(email));
    }

    @Test
    void shouldThrowFieldTooLongExceptionOnCheckIfEmailWithinLimit() {
        int length = MAX_EMAIL_LENGTH + 1;
        String email = RandomStringUtils.random(length);

        assertThrows(FieldTooLongException.class, () -> validationUtil.checkIfEmailWithinLimit(email));
    }

    @Test
    void checkIfRoleNameWithinLimit() {
        String name = RandomStringUtils.random(MAX_ROLE_NAME_LENGTH);

        assertDoesNotThrow(() -> validationUtil.checkIfRoleNameWithinLimit(name));
    }

    @Test
    void shouldThrowFieldTooLongExceptionOnCheckIfRoleNameWithinLimit() {
        int length = MAX_ROLE_NAME_LENGTH + 1;
        String email = RandomStringUtils.random(length);

        assertThrows(FieldTooLongException.class, () -> validationUtil.checkIfRoleNameWithinLimit(email));
    }

    @Test
    void checkIfDepartmentNameWithinLimit() {
        String name = RandomStringUtils.random(MAX_DEPARTMENT_NAME_LENGTH);

        assertDoesNotThrow(() -> validationUtil.checkIfDepartmentNameWithinLimit(name));
    }

    @Test
    void shouldThrowExceptionOnCheckIfDepartmentNameWithinLimit() {
        int length = MAX_DEPARTMENT_NAME_LENGTH + 1;
        String name = RandomStringUtils.random(length);

        assertThrows(FieldTooLongException.class, () -> validationUtil.checkIfDepartmentNameWithinLimit(name));
    }

    @Test
    void isDepartmentHead() {
        Role invalidRole = new Role();
        invalidRole.setAuthorities(new AuthorityType[0]);
        Role validRole = new Role();
        validRole.setAuthorities(HEAD_AUTHORITIES.toArray(new AuthorityType[0]));

        assertFalse(validationUtil.isDepartmentHead(invalidRole));
        assertTrue(validationUtil.isDepartmentHead(validRole));
    }

    @Test
    void isRmRole() {
        Role role = new Role();
        when(roleValidationUtil.isRmRole(any(Role.class))).thenReturn(anyBoolean());

        assertDoesNotThrow(() -> validationUtil.isRmRole(role));
        verify(roleValidationUtil).isRmRole(isA(Role.class));
    }
}