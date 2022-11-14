package com.rm.toolkit.user.command.dto.command.user;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateUserCommand {

    @Schema(example = "Елизавета")
    @NotNull
    @Size(max = 1000)
    protected String firstName;

    @Schema(example = "Джаваскриптова")
    @NotNull
    @Size(max = 1000)
    protected String lastName;

    @Schema(example = "js_ftw@rmtm.work")
    @Email(message = "Неверный формат ввода электронной почты")
    @Size(max = 35)
    @NotBlank
    protected String email;

    @Schema(description = "Пока что возможости залить аватарку нет, так что передавайте null без кавычек",
            example = "null")
    protected String avatarPath;

    @Schema(example = "cf8a4b92-8c2c-40bb-8403-cfa0964e7c47")
    protected String cityId;

    @Schema(example = "62f69579-ad7f-4dfd-9fea-b718d7c0d968")
    @NotBlank
    protected String departmentId;

    @Schema(example = "7fbdcccb-f166-49af-ac63-a14d87a0d914")
    @NotBlank
    protected String resourceManagerId;

    @Schema(example = "579fc993-6123-419a-ae3c-96b0b230f834")
    @NotBlank
    protected String roleId;

    @ArraySchema(schema = @Schema(example = "99e2f6b1-d90b-4d23-a03c-63d878298757"))
    @NotNull
    protected Set<String> activitiesIds;
}
