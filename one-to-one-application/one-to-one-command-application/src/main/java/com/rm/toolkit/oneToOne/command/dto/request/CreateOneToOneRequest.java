package com.rm.toolkit.oneToOne.command.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOneToOneRequest {

    private String userId;

    private ZonedDateTime dateTime;

    private String comment;
}
