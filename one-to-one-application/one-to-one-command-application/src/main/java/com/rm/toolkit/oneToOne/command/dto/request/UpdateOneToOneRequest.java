package com.rm.toolkit.oneToOne.command.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UpdateOneToOneRequest {

    protected ZonedDateTime dateTime;

    protected String comment;
}
