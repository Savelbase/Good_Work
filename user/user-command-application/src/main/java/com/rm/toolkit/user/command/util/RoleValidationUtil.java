package com.rm.toolkit.user.command.util;

import com.rm.toolkit.user.command.exception.conflict.RoleAlreadyExistsException;
import com.rm.toolkit.user.command.exception.conflict.UserCantBecomeDepartmentHeadException;
import com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException;
import com.rm.toolkit.user.command.model.Role;
import com.rm.toolkit.user.command.repository.RoleRepository;
import com.rm.toolkit.user.command.security.AuthorityType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleValidationUtil {
    private final RoleRepository roleRepository;

    @AllArgsConstructor
    private enum ValidRole {
        ARM("ARM", 3),
        RM("RM", 5),
        SRM("SRM", 7),
        RD("RD", 9);

        private final String roleName;
        private final int rolePriority;
    }

    public static final List<AuthorityType> ADMIN_AUTHORITIES = List.of(AuthorityType.EDIT_ROLES,
            AuthorityType.EDIT_DEPARTMENTS, AuthorityType.EDIT_INTERVALS);

    /**
     * @param role проверяемая роль
     * @return может ли пользователь стать главой отдела
     */
    public boolean canBecomeDepartmentHead(Role role) {
        return Arrays.stream(ValidRole.values())
                .anyMatch(v -> v.rolePriority == role.getPriority() && v.roleName.equals(role.getName()));
    }

    public boolean isRm(Role role) {
        return Arrays.asList(role.getAuthorities()).contains(AuthorityType.ONE_TO_ONE);
    }

    public boolean isAdmin(Role role) {
        return Arrays.stream(role.getAuthorities()).collect(Collectors.toSet()).containsAll(ADMIN_AUTHORITIES);
    }

    public boolean isAdmin(Collection<AuthorityType> authorities) {
        return authorities.stream().anyMatch(r -> AuthorityType.EDIT_INTERVALS.equals(r)
                || AuthorityType.EDIT_DEPARTMENTS.equals(r) || AuthorityType.EDIT_ROLES.equals(r));
    }

    /**
     * Проверка оперирования с ролью выше или равной своей.
     *
     * @param roleId             id роли с которой сравнивается приоритет автора команды
     * @param authorRolePriority приоритет роли автора команды
     * @param authorId           id автора команды
     * @throws org.springframework.security.access.AccessDeniedException автор попытался оперировать с ролью выше или равной своей
     */
    public void checkIfOtherRolePriorityHigherOrEqualsThanAuthors(String roleId, Integer authorRolePriority, String authorId) {
        Role oldRole = findRoleIfExist(roleId);
        if (oldRole.getPriority() >= authorRolePriority) {
            log.info("Пользователь с id {} не смог изменить пользователя или установить ресурсного менеджера с ролью, выше своей.", authorId);
            throw new AccessDeniedException("Нельзя изменить пользователя, приоритет роли которого >= вашего.");
        }
    }

    /**
     * Проверка оперирования с ролью выше своей. Отличается от метода checkIfOtherRolePriorityHigherOrEqualsThanAuthors,
     * для того чтобы можно было назначать себя ресурсным менеждером.
     *
     * @param roleId             id роли с которой сравнивается приоритет автора команды
     * @param authorRolePriority приоритет роли автора команды
     * @param authorId           id автора команды
     * @throws org.springframework.security.access.AccessDeniedException автор попытался оперировать с ролью выше своей
     */
    public void checkIfOtherRolePriorityHigherThanAuthors(String roleId, Integer authorRolePriority, String authorId) {
        Role oldRole = findRoleIfExist(roleId);
        if (oldRole.getPriority() > authorRolePriority) {
            log.info("Пользователь с id {} не смог изменить пользователя или установить ресурсного менеджера с ролью, выше своей.", authorId);
            throw new AccessDeniedException("Нельзя изменить пользователя, приоритет роли которого > вашего.");
        }
    }

    /**
     * @param priority приоритет роли
     * @throws com.rm.toolkit.user.command.exception.unprocessableentity.RolePriorityOutOfBoundsException создание роли с приоритетом вне диапазона
     */
    public void checkIfRolePriorityWithinBounds(Integer priority) {
        if (priority < 1 || priority > 9) throw new RolePriorityOutOfBoundsException();
    }

    /**
     * @param roleName имя роли
     * @throws com.rm.toolkit.user.command.exception.conflict.RoleAlreadyExistsException роль с таким именем уже существует
     */
    public void checkThatRoleDoesntExist(String roleName) {
        if (roleRepository.existsByNameIgnoreCase(roleName)) {
            log.error("Роль {} уже существует", roleName);
            throw new RoleAlreadyExistsException(roleName);
        }
    }

    /**
     * @param roleId id роли
     * @throws com.rm.toolkit.user.command.exception.notfound.RoleNotFoundException роль с таким id не найдена
     */
    public Role findRoleIfExist(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> {
            log.error("Роль с id {} не найден", roleId);
            throw new RoleNotFoundException(roleId);
        });
    }

    /**
     * @param role проверяемая роль
     * @throws UserCantBecomeDepartmentHeadException пользователь не может стать главой отдела
     */
    public void checkIfCanBecomeDepartmentHead(Role role) {
        if (!canBecomeDepartmentHead(role)) {
            log.info("Роль c id {} не имеет достаточных прав, чтобы кто-то мог стать с ней главой отдела", role.getId());
            throw new UserCantBecomeDepartmentHeadException(role.getId());
        }
    }

    public boolean isRmRole(Role role) {
        return canBecomeDepartmentHead(role);
    }
}


