package com.rm.toolkit.user.command.dto.command.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangeUserDepartmentCommand {

    @Schema(example = "a1fa6daf-f31f-4fa3-87f2-797467743f0c")
    @NotBlank
    protected String departmentId;
}
