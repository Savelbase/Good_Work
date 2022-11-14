package com.rm.toolkit.oneToOne.query.dto.response.error;

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
        ONE_TO_ONE_NOT_FOUND_BY_ID,
        ONE_TO_ONE_FOR_USER_NOT_FOUND_BY_ID,
        ONE_TO_ONE_FOR_RM_NOT_FOUND_BY_ID,
        USER_NOT_FOUND_BY_ID
    }
}
