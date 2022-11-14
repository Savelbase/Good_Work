package com.rm.toolkit.user.command.dto.command.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangeUsersDepartmentCommand {

    @NotNull(message = "Список членов отдела не передан")
    protected Set<String> employees;
}
