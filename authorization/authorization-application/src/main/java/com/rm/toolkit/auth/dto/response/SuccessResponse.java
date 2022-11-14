package com.rm.toolkit.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SuccessResponse {

    @Schema(example = "Операция успешно выполнена")
    protected String message;

    public static SuccessResponse getGeneric() {
        return new SuccessResponse("Операция успешно выполнена");
    }
}
