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
public class NotFoundErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    private ErrorType errorType;

    public enum ErrorType {
        ACTIVITY_NOT_FOUND_BY_ID,
        AUTHORITY_NOT_FOUND_BY_ID,
        CITY_NOT_FOUND_BY_ID,
        COUNTRY_NOT_FOUND_BY_ID,
        DEPARTMENT_NOT_FOUND_BY_ID,
        ROLE_NOT_FOUND_BY_ID,
        USER_NOT_FOUND_BY_ID
    }
}
