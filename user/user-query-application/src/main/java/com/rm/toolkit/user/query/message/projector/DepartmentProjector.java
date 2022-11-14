package com.rm.toolkit.user.query.message.projector;

import com.rm.toolkit.user.query.event.department.DepartmentCreatedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentDeletedEvent;
import com.rm.toolkit.user.query.event.department.DepartmentEditedEvent;
import com.rm.toolkit.user.query.model.Department;
import com.rm.toolkit.user.query.model.embedded.UserEmbedded;
import com.rm.toolkit.user.query.util.ProjectionUtil;
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
                .head(new UserEmbedded(payload.getHeadId(), payload.getHeadFirstName(),
                        payload.getHeadLastName()))
                .version(1)
                .build();
    }

    public void project(DepartmentEditedEvent event, Department department) {
        projectionUtil.validateEvent(event, department.getId(), department.getVersion());

        var payload = event.getPayload();

        department.setName(event.getPayload().getName());
        department.setHead(new UserEmbedded(payload.getHeadId(), payload.getHeadFirstName(),
                payload.getHeadLastName()));
        projectionUtil.incrementVersion(department);
    }

    public void project(DepartmentDeletedEvent event, Department department) {
        projectionUtil.validateEvent(event, department.getId(), department.getVersion());

        department.setDeleted(true);
        projectionUtil.incrementVersion(department);
    }
}
