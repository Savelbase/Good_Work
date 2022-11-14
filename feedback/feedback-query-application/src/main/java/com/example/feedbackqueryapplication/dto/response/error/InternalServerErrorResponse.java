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
public class InternalServerErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    @Schema(example = "Подробное сообщение об ошибке")
    private String message;
}
