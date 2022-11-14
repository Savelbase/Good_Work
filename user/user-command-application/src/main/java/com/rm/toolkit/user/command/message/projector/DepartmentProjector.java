package com.rm.toolkit.user.command.message.projector;

import com.rm.toolkit.user.command.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.command.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.command.model.Department;
import com.rm.toolkit.user.command.util.ProjectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentProjector {

    private final ProjectionUtil projectionUtil;

    public Department project(DepartmentCreatedEvent event) {
        var payload = event.getPayload();

        return Department.builder()
                .id(event.getEntityId())
                .name(payload.getName())
                .deletable(payload.isDeletable())
                .headId(payload.getHeadId())
                .version(1)
                .build();
    }

    public void project(DepartmentEditedEvent event, Department department) {
        projectionUtil.validateEvent(event, department.getId(), department.getVersion());

        var payload = event.getPayload();

        department.setName(payload.getName());
        department.setHeadId(payload.getHeadId());
        projectionUtil.incrementVersion(department);
    }

    public void project(DepartmentDeletedEvent event, Department department) {
        projectionUtil.validateEvent(event, department.getId(), department.getVersion());

        department.setDeleted(true);
        projectionUtil.incrementVersion(department);
    }

}
