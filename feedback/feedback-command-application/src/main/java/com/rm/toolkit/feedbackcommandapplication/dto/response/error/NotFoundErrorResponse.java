package com.rm.toolkit.feedbackcommandapplication.dto.response.error;

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
        FEEDBACK_NOT_FOUND_BY_ID,
        USER_NOT_FOUND_BY_ID,
        DEPARTMENT_NOT_FOUND_BY_ID,
        ONE_TO_ONE_NOT_FOUND_BY_ID
    }
}
