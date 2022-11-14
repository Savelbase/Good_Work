package com.rm.toolkit.mediaStorage.command.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SuccessResponse {

    @Schema(example = "Операция успешно выполнена")
    protected String message = "Операция успешно выполнена";
}
