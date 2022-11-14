package com.rm.toolkit.user.query.service;

import com.rm.toolkit.user.query.exception.notfound.RoleNotFoundException;
import com.rm.toolkit.user.query.model.Role;
import com.rm.toolkit.user.query.model.RoleMinimalInfo;
import com.rm.toolkit.user.query.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleQueryService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleMinimalInfo> getAllRoles() {
        return roleRepository.findAllMinimalInfo();
    }

    @Transactional(readOnly = true)
    public List<RoleMinimalInfo> getAvailableForCreationRoles(int currentUserPriority) {
        return roleRepository.findAllByPriorityLessThanMinimalInfo(currentUserPriority);
    }

    @Transactional(readOnly = true)
    public Role getRoleFullInfo(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> {
            log.info("Роль с id {} не найдена", roleId);
            throw new RoleNotFoundException(roleId);
        });
    }
}
