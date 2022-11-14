package com.rm.toolkit.feedbackcommandapplication.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class EmailCommandPayload {

    protected CommandType type;

    protected String oneToOneId;

    protected String subordinateFirstName;

    protected String subordinateLastName;

    protected String subordinateEmail;

    protected String resourceManagerFirstName;

    protected String resourceManagerLastName;

    protected String resourceManagerEmail;

    protected ZonedDateTime dateTime;

    protected String comment;

    protected boolean isDeleted;
}