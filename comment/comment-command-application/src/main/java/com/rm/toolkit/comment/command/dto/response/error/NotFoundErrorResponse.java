package com.rm.toolkit.comment.command.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundErrorResponse {


    private Date timestamp;

    private ErrorType errorType;

    public enum ErrorType {
        COMMENT_NOT_FOUND_BY_ID
    }
}
