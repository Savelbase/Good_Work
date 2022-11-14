package com.rm.toolkit.user.command.dto.command.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EditDepartmentCommand {

    @NotBlank
    @Size(max = 20)
    protected String name;

    @NotBlank
    protected String headId;
}
