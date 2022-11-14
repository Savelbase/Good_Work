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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidationUtil {

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

    public boolean isRm(Role role) {
        return roleValidationUtil.isRm(role);
    }

    public boolean isAdmin(Role role) {
        return roleValidationUtil.isAdmin(role);
    }

    public boolean isAdmin(Collection<AuthorityType> authorities) {
        return roleValidationUtil.isAdmin(authorities);
    }

    /**
     * @param userId id пользователя
     * @throws com.rm.toolkit.user.command.exception.notfound.UserNotFoundException пользователь с таким id не найден
     */
    public User findUserIfExist(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("Пользователь с id {} не найден", userId);
            throw new UserNotFoundException(userId);
        });
    }

    /**
     * @param cityId id города
     * @throws com.rm.toolkit.user.command.exception.notfound.CityNotFoundException город с таким id не найдена
     */
    public City findCityIfExist(String cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> {
            log.error("Город с id {} не найден", cityId);
            throw new CityNotFoundException(cityId);
        });
    }

    /**
     * @param countryId id страны
     * @throws com.rm.toolkit.user.command.exception.notfound.CountryNotFoundException страна с таким id не найдена
     */
    public Country findCountryIfExist(String countryId) {
        return countryRepository.findById(countryId).orElseThrow(() -> {
            log.error("Страна с id {} не найдена", countryId);
            throw new CountryNotFoundException(countryId);
        });
    }

    /**
     * @param departmentId id отдела
     * @throws com.rm.toolkit.user.command.exception.notfound.DepartmentNotFoundException отдел с таким id не найден
     */
    public Department findDepartmentIfExist(String departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(() -> {
            log.error("Отдел с id {} не найден", departmentId);
            throw new DepartmentNotFoundException(departmentId);
        });
    }

    /**
     * @param roleId id роли
     * @throws com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException роль с таким id не найдена
     */
    public Role findRoleIfExist(String roleId) {
        return roleValidationUtil.findRoleIfExist(roleId);
    }

    /**
     * @param activityIds перечень id активностей
     * @throws com.rm.toolkit.user.command.exception.notfound.ActivityNotFoundException активность по id не найдена
     */
    public Set<Activity> findUserActivities(Set<String> activityIds) {
        return activityIds.stream()
                .map(a -> activityRepository.findById(a).orElseThrow(() -> {
                    log.error("Активность с id {} не найдена", a);
                    throw new ActivityNotFoundException(a);
                })).collect(Collectors.toSet());
    }

    /**
     * @param authorityTypes список доступов
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.OnlyAdminShouldHaveSettingsAuthorityException попытка добавить доступ, доступный только Admin
     */
    public void validateAuthorities(Set<AuthorityType> authorityTypes) {
        if (isAdmin(authorityTypes)) {
            throw new OnlyAdminShouldHaveSettingsAuthorityException();
        }
    }

    /**
     * @param email email пользователя
     * @throws com.rm.toolkit.user.command.exception.conflict.UserAlreadyExistException пользователь с таким email уже существует
     */
    public void checkThatUserDoesntExist(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            log.error("Пользователь с email {} уже существует", email);
            throw new UserAlreadyExistException(email);
        }
    }

    /**
     * @param departmentName имя отдела
     * @throws com.rm.toolkit.user.command.exception.conflict.DepartmentAlreadyExistsException отдел с таким именем уже существует
     */
    public void checkThatDepartmentDoesntExist(String departmentName) {
        if (departmentRepository.existsByNameIgnoreCase(departmentName)) {
            log.error("Отдел {} уже существует", departmentName);
            throw new DepartmentAlreadyExistsException(departmentName);
        }
    }

    /**
     * @param roleName имя роли
     * @throws com.rm.toolkit.user.command.exception.conflict.RoleAlreadyExistsException роль с таким именем уже существует
     */
    public void checkThatRoleDoesntExist(String roleName) {
        if(roleName==null){
            throw new BadRequestException("RoleName is null");
        }
        roleValidationUtil.checkThatRoleDoesntExist(roleName);
    }

    /**
     * @param status поле статус команды
     * @throws com.rm.toolkit.user.command.exception.badrequest.BadRequestException поле статус не заполнено
     */
    public void checkStatusForNull(StatusType status) {
        if (status == null) {
            log.info("Статус == null");
            throw new BadRequestException("Поле status не заполнено");
        }
    }

    /**
     * @param priority       приоритет новой роли
     * @param authorPriority приоритет автора команды
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException создание роли с приоритетом вне диапазона
     */
    public void checkIfAllowedToCreateUserWithThisRole(Integer priority, Integer authorPriority) {
        if (priority >= authorPriority) {
            throw new AccessDeniedException("Нельзя создать пользователя, приоритет роли которого >= вашего");
        }
    }

    /**
     * @param priority приоритет роли
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException создание роли с приоритетом вне диапазона
     */
    public void checkIfRolePriorityWithinBounds(Integer priority) {
        roleValidationUtil.checkIfRolePriorityWithinBounds(priority);
    }

    /**
     * @param authorId           id автора команды
     * @param authorAuthorities  список доступов автора команды
     * @param authorRolePriority приоритет роли автора команды
     * @param roleId             id роли, присваемой пользователю
     * @param departmentId       id отдела, присваемого пользователю
     * @throws org.springframework.security.access.AccessDeniedException недостаточно прав для выполнения команды
     */
    public void checkPermissionsForManipulatingUser(String authorId, Collection<AuthorityType> authorAuthorities,
                                                    int authorRolePriority, String roleId, String departmentId) {
        User creator = findUserIfExist(authorId);

        if (!authorAuthorities.contains(AuthorityType.EMPLOYEE_CARD)) {
            log.info("У пользователя с id {} не хватило прав для создания нового пользователя", authorId);
            throw new AccessDeniedException("Нет прав для создания пользователя");
        }

        // Право SETTINGS есть только у админов, поэтому им можно создавать пользователей в любой отдел
        if (!isAdmin(authorAuthorities) && !creator.getDepartmentId().equals(departmentId)) {
            log.info("Пользователь с id {} не смог создать пользователя в чужой отдел", authorId);
            throw new AccessDeniedException("Нельзя создать пользователя не в ваш отдел");
        }

        Role newUserRole = findRoleIfExist(roleId);
        if (newUserRole.getPriority() >= authorRolePriority)
            throw new AccessDeniedException("Нельзя поставить пользователю роль с приоритетом >= вашего");
    }

    public void checkIfCanBecomeDepartmentHead(Role role) {
        roleValidationUtil.checkIfCanBecomeDepartmentHead(role);
    }

    /**
     * @param user проверяемый пользователь
     * @throws UserHasDepartmentsException пользователь уже является главой отдела
     */
    public void checkIfCanBecomeNewDepartmentHead(User user) {
        if (!isAlreadyHeadOfDepartment(user)) {
            Role role = findRoleIfExist(user.getRoleId());
            checkIfCanBecomeDepartmentHead(role);
        } else {
            log.info("Пользователь c id {} уже глава отдела", user.getId());
            throw new UserHasDepartmentsException(user.getId());
        }
    }

    /**
     * @param user проверяемый пользователь
     * @return true, если пользователь уже является главой отдела
     */
    private boolean isAlreadyHeadOfDepartment(User user) {
        return !departmentRepository.findAllByHeadId(user.getId()).isEmpty();
    }

    /**
     * @param user проверяемый пользователь
     * @throws com.rm.toolkit.user.command.exception.conflict.UserHasDepartmentsException пользователь является главой отдела
     */
    public void userShouldNotBeDepartmentHead(User user) {
        if (isAlreadyHeadOfDepartment(user)) {
            throw new UserHasDepartmentsException(user.getId());
        }
    }

    public void checkIfOtherRolePriorityHigherThanAuthors(String roleId, Integer authorRolePriority, String authorId) {
        roleValidationUtil.checkIfOtherRolePriorityHigherThanAuthors(roleId, authorRolePriority, authorId);
    }

    public void checkIfOtherRolePriorityHigherOrEqualsThanAuthors(String roleId, Integer authorRolePriority, String authorId) {
        roleValidationUtil.checkIfOtherRolePriorityHigherOrEqualsThanAuthors(roleId, authorRolePriority, authorId);
    }

    /**
     * Проверка, что поле email не превышает ограничение.
     *
     * @param email значение email
     * @throws com.rm.toolkit.user.command.exception.badrequest.FieldTooLongException длина поля email больше ограничения
     */
    public void checkIfEmailWithinLimit(String email) {
        if(email==null){
            throw new BadRequestException("Email is null");
        }
        if (email.length() > MAX_EMAIL_LENGTH) {
            log.error("Слишком длинное поле email {}", email);
            throw new FieldTooLongException(email);
        }
    }

    /**
     * Проверка, что поле название роли не превышает ограничение.
     *
     * @param roleName значение название роли
     * @throws com.rm.toolkit.user.command.exception.badrequest.FieldTooLongException длина поля название роли больше ограничения
     */
    public void checkIfRoleNameWithinLimit(String roleName) {
        if(roleName==null){
            throw new BadRequestException("RoleName is null");
        }
        if (roleName.length() > MAX_ROLE_NAME_LENGTH) {
            log.error("Слишком длинное имя роли {}", roleName);
            throw new FieldTooLongException(roleName);
        }
    }

    /**
     * Проверка, что поле название отдела не превышает ограничение.
     *
     * @param departmentName значение названия отдела
     * @throws com.rm.toolkit.user.command.exception.badrequest.FieldTooLongException длина поля название отдела больше ограничения
     */
    public void checkIfDepartmentNameWithinLimit(String departmentName) {
        if(departmentName==null){
            throw new BadRequestException("DepartmentName is null");
        }
        if (departmentName.length() > MAX_DEPARTMENT_NAME_LENGTH) {
            log.error("Слишком длинное имя отдела {}", departmentName);
            throw new FieldTooLongException(departmentName);
        }
    }

    /**
     * @param role проверяемая роль
     * @return является ли роль главой отдела
     */
    public boolean isDepartmentHead(Role role) {
        return Arrays.asList(role.getAuthorities()).containsAll(HEAD_AUTHORITIES);
    }

    public boolean isRmRole(Role role) {
        return roleValidationUtil.isRmRole(role);
    }
}
