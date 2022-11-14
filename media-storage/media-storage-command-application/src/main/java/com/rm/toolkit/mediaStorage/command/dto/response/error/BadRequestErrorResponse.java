package com.rm.toolkit.mediaStorage.command.dto.response.error;

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
public class BadRequestErrorResponse {

    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    @Schema(example = "Поле не заполнено")
    private String message;

    public enum ErrorType {
        WRONG_EXTENSION,
        FILE_NOT_PROVIDED
    }
}
