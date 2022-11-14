package com.rm.toolkit.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LogoutRequest {

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJybXRfbW9iaWxlX2F1dGgiLCJzdWIiOiJlNDViZmZmMC0xYjA0LTRiOGEtYWM5" +
            "NS05NjI5ZGZmODhhM2UiLCJpYXQiOjE2MzM3ODE4MjUsImV4cCI6MTYzMzgxNDIyNX0.Sf7oZFi_aNYbzYdCGZVAm5eb1rq9sfQ1WdJ" +
            "2Kl95vHXzCCmuck5Rzp7Fn1c0L40dMpvBtQ56uf3nbk853HYhjg")
    @NotBlank
    private String refreshToken;
}
