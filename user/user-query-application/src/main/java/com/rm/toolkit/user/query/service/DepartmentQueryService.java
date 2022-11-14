package com.rm.toolkit.user.query.service;

import com.rm.toolkit.user.query.exception.notfound.DepartmentNotFoundException;
import com.rm.toolkit.user.query.model.Department;
import com.rm.toolkit.user.query.model.DepartmentMinimalInfo;
import com.rm.toolkit.user.query.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentQueryService {

    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<DepartmentMinimalInfo> getAllDepartments() {
        return departmentRepository.findAllMinimalInfo();
    }

    @Transactional(readOnly = true)
    public Department getDepartmentFullInfo(String departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(() -> {
            log.info("Отдел с id {} не найден", departmentId);
            throw new DepartmentNotFoundException(departmentId);
        });
    }

    @Transactional(readOnly = true)
    public Department getNoDepartmentId() {
        String NO_DEPARTMENT = "No department";
        return departmentRepository.findDepartmentByName(NO_DEPARTMENT);
    }
}
