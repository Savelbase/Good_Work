package com.example.feedbackqueryapplication.dto.response.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotFoundErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    private ErrorType errorType;

    public enum ErrorType {
        USER_NOT_FOUND_BY_ID,
        FEEDBACK_NOT_FOUND_BY_ID
    }
}
