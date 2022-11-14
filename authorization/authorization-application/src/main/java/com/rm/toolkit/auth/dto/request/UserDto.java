package com.rm.toolkit.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDto {

    @Schema(example = "rd_qa@rmtm.work")
    @NotEmpty(message = "Логин не может быть пустым")
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @Schema(example = "qwerty")
    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;
}
