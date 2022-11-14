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
public class UnauthorizedErrorResponse {

    private Date timestamp;
    private ErrorType errorType;

    public enum ErrorType {
        INVALID_ACCESS_TOKEN,
        EXPIRED_ACCESS_TOKEN
    }
}
