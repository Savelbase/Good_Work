package com.rm.toolkit.comment.query.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadRequestErrorResponse {

    private Date timestamp;
    private String message;
}
