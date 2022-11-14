package com.rm.toolkit.auth.dto.response;

import com.rm.toolkit.auth.dto.response.error.UnauthorizedErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class LoginErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    protected Date timestamp;

    @Schema(example = "INCORRECT_PASSWORD")
    private UnauthorizedErrorResponse.ErrorType errorType = UnauthorizedErrorResponse.ErrorType.INCORRECT_PASSWORD;

    @Schema(example = "4")
    private Integer remainingLoginAttempts;
}
