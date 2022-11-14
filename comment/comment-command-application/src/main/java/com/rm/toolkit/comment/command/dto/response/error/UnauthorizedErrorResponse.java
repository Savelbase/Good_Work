package com.rm.toolkit.comment.command.dto.response.error;

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
public class UnauthorizedErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    private ErrorType errorType;

    public enum ErrorType {
        EXPIRED_ACCESS_TOKEN,
        INVALID_ACCESS_TOKEN
    }
}
