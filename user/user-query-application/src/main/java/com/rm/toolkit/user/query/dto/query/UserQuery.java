package com.rm.toolkit.user.query.dto.query;

import com.rm.toolkit.user.query.model.type.StatusType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UserQuery {

    @Parameter(description = "Строка, которая ищется в начале фамилии и имени, case-insensitive")
    protected String fullName;

    @Parameter(description = "Сортировать по имени, если фолс то по фамилии")
    protected boolean sortByName = false;

    protected Set<String> departmentId;

    protected Set<String> roleId;

    protected Set<String> activityId;

    @Parameter(description = "Показывать только подопечных этого RMа")
    protected String resourceManagerId;

    @Parameter(description = "Показывать всех подопечных подопечных (true) или только прямых подопечных (false)")
    protected boolean recursive = false;

    @Parameter(description = "Показывать только RMов")
    protected boolean rmsOnly = false;

    @Parameter(description = "Показать Employee")
    protected boolean showEmployee = false;

    @Parameter(description = "Показать ARM")
    protected boolean showArm = false;

    @Parameter(description = "Показать RM")
    protected boolean showRm = false;

    @Parameter(description = "Показать SRM")
    protected boolean showSrm = false;

    @Parameter(description = "Показать RD")
    protected boolean showRd = false;

    @Parameter(description = "Показать Admin")
    protected boolean showAdmin = false;

    @Parameter(description = "По статусу фильтровать могут только те роли, которые имеют право менять статусы (по-умолчанию Admin и RD)")
    protected StatusType status;
}
