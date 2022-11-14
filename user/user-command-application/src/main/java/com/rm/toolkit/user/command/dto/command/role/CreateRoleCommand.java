package com.rm.toolkit.user.command.dto.command.role;

import com.rm.toolkit.user.command.security.AuthorityType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
public class CreateRoleCommand {

    @Schema(example = "Мимокрокодил")
    @NotBlank(message = "Название роли не может быть пустым")
    @Size(max = 20)
    protected String name;

    @Schema(example = "5")
    @NotNull(message = "Приоритет роли не передан")
    protected Integer priority;

    @NotNull(message = "Список прав не передан")
    protected Set<AuthorityType> authorities;
}
