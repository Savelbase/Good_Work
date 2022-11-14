package com.rm.toolkit.mediaStorage.query.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {

    @Schema(example = "Операция успешно выполнена")
    protected String message = "Операция успешно выполнена";
}
