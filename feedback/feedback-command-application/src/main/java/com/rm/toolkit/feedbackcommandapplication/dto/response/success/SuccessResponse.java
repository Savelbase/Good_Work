package com.rm.toolkit.feedbackcommandapplication.dto.response.success;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {

    @Schema(example = "Операция успешно выполнена")
    private String message;

    public static SuccessResponse getGeneric() {
        return new SuccessResponse("Операция успешно выполнена");
    }
}
