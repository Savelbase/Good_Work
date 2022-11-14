package com.rm.toolkit.user.command.dto.response.error;

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
public class UnprocessableEntityErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    private ErrorType errorType;

    public enum ErrorType {
        ONLY_ADMIN_SHOULD_HAVE_SETTINGS_AUTHORITY,
        ROLE_PRIORITY_OUT_OF_BOUNDS,
        ADMIN_ROLE_IS_NOT_EDITABLE,
        BASE_ROLE_NOT_DELETABLE,
        BASE_ROLE_NOT_EDITABLE
    }
}
