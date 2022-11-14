package com.rm.toolkit.feedbackcommandapplication.util;

import com.rm.toolkit.feedbackcommandapplication.exception.notfound.DepartmentNotFoundException;
import com.rm.toolkit.feedbackcommandapplication.model.Department;
import com.rm.toolkit.feedbackcommandapplication.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentUtil {

    private final DepartmentRepository departmentRepository;

    public Department findDepartmentIfExists(String departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> {
                    log.info("Отдел с id {} не найден", departmentId);
                    throw new DepartmentNotFoundException(departmentId);
                });
    }
}