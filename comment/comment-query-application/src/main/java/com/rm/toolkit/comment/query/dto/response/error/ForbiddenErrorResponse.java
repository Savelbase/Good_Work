package com.rm.toolkit.comment.query.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForbiddenErrorResponse {

    private Date timestamp;
    private ErrorType errorType;

    public enum ErrorType {
        NOT_ENOUGH_RIGHTS
    }
}
