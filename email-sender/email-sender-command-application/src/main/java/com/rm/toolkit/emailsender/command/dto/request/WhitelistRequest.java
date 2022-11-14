package com.rm.toolkit.emailsender.command.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class WhitelistRequest {

    @Schema(example = "srm_qa@rmtm.work")
    @NotBlank
    @Email
    private String email;
}