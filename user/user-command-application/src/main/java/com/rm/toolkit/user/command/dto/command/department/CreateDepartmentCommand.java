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
public class CreateDepartmentCommand {

    @NotBlank
    @Size(max = 20)
    protected String name;

    @Schema(example = "0922737d-2b19-456b-949b-15740eea99d3")
    @NotBlank
    protected String headId;

    @NotNull(message = "Список членов отдела не передан")
    protected Set<String> membersIds;
}
