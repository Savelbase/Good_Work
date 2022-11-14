package com.rm.toolkit.mediaStorage.command.dto.response.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileTooLargeErrorResponse {
    @Schema(example = "2021-08-20T13:37:19.408+00:00")
    private Date timestamp;

    private String message;
}
